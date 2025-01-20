package com.lox.parser;

import java.util.ArrayList;
import java.util.List;

import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.Stmt;

public class LoxGrammar {

    // ===== HELPER METHODS =====
    
    private void expectSemicolon (LoxParser.Context ctx) {
        ctx.matchOrThrow(LoxTokenType.SEMICOLON, "expected ';' after statements");
    }

    // ===== GRAMMAR =====

    public List<Stmt> start (LoxParser.Context ctx) {
        List<Stmt> statements = new ArrayList<>();
        while (!ctx.isAtEnd()) {
            statements.add(this.declaration(ctx));
        }
        
        return statements;
    }

    private Stmt declaration (LoxParser.Context ctx) {
        Stmt stmt = null;
        if (ctx.lookahead(LoxTokenType.VAR)) {
            stmt = this.varDeclStatement(ctx);
        } else {
            stmt = this.statement(ctx);
        }

        if (stmt == null) {
            throw ctx.error(ctx.getLastMatchedToken(), "invalid declaration");
        } else {
            return stmt;
        }
    }

    private Stmt statement (LoxParser.Context ctx) {
        if (ctx.lookahead(LoxTokenType.LEFT_BRACE)) {
            return this.blockStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.PRINT)) {
            return this.printStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.IF)) {
            return this.ifStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.WHILE)) {
            return this.whileStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.FOR)) {
            return this.forStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.BREAK)) {
            return this.breakStatement(ctx);
        } else if (ctx.lookahead(LoxTokenType.CONTINUE)) {
            return this.continueStatement(ctx);
        } else {
            return this.expressionStatement(ctx);
        }
    }

    // ===== IF-ELSE CONTROL FLOW =====

    private Stmt ifStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.IF);
        ctx.matchOrThrow(LoxTokenType.LEFT_PAREN, "expected expression");
        Expr ifCondition = this.expression(ctx);
        ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected expression");
        Stmt ifStatement = this.statement(ctx);

        // match else if statements
        List<Stmt.IfStmt> elseIfStatements = new ArrayList<>();
        while (ctx.kLookahead(LoxTokenType.ELSE, LoxTokenType.IF)) {
            elseIfStatements.add(this.elseIfStatement(ctx));
        }

        // match else statement
        Stmt elseStatement = null;
        if (ctx.match(LoxTokenType.ELSE)) {
            elseStatement = this.statement(ctx);
        }

        return new Stmt.IfStmt(ifCondition, ifStatement, elseIfStatements, elseStatement);
    }

    private Stmt.IfStmt elseIfStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.ELSE);
        ctx.match(LoxTokenType.IF);
        ctx.matchOrThrow(LoxTokenType.LEFT_PAREN, "expected expression");
        Expr elseIfCondition = this.expression(ctx);
        ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected expression");
        Stmt elseIfStatement = this.statement(ctx);
        return new Stmt.IfStmt(elseIfCondition, elseIfStatement);
    }

    // ===== LOOPS =====

    private Stmt whileStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.WHILE);
        ctx.matchOrThrow(LoxTokenType.LEFT_PAREN, "expected expression");
        Expr condition = expression(ctx);
        ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected expression");
        Stmt whileStatement = statement(ctx);
        return new Stmt.WhileStmt(condition, whileStatement);
    }

    private Stmt forStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.FOR);

        // match clauses
        ctx.matchOrThrow(LoxTokenType.LEFT_PAREN, "expected loop 'initializer', 'condition', and 'increment' clauses");
        Stmt initializer = forInitializer(ctx);

        Expr condition = null;
        if (!ctx.lookahead(LoxTokenType.SEMICOLON)) {
            condition = expression(ctx);
        }
        ctx.matchOrThrow(LoxTokenType.SEMICOLON, "for loop expects an 'increment' clause");
        
        Expr increment = null;
        if (!ctx.lookahead(LoxTokenType.RIGHT_PAREN)) {
            increment = expression(ctx); 
        }
        ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "unclosed loop clause");

        // match statement
        Stmt forStatement = statement(ctx);

        // desugar
        List<Stmt> loopBody = new ArrayList<>();
        loopBody.add(forStatement);
        if (increment != null) {
            loopBody.add(new Stmt.ExpressionStmt(increment));
        }

        List<Stmt> loopStatements = new ArrayList<>();
        if (initializer != null) {
            loopStatements.add(initializer);
        }
        loopStatements.add(new Stmt.WhileStmt(condition, new Stmt.BlockStmt(loopBody)));

        Stmt desugaredForLoopBlock = new Stmt.BlockStmt(loopStatements);
        return desugaredForLoopBlock;
    }

    private Stmt forInitializer (LoxParser.Context ctx) {
        if (ctx.lookahead(LoxTokenType.VAR)) {
            return varDeclStatement(ctx);
        } else if (ctx.match(LoxTokenType.SEMICOLON)) {
            return null;
        } else {
            return expressionStatement(ctx);
        }
    }

    // ===== DECLARATIONS =====

    private Stmt varDeclStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.VAR);
        ctx.matchOrThrow(LoxTokenType.IDENTIFIER, "expected variable name");
        LoxToken identifier = ctx.getLastMatchedToken();

        Stmt varDeclStatement;
        if (ctx.match(LoxTokenType.EQUAL)) {
            // expecting a right-hand expression
            Expr expr = this.expression(ctx);
            varDeclStatement = new Stmt.VarDeclStmt(identifier, expr);
        } else {
            // an uninitialized variable declaration
            varDeclStatement = new Stmt.VarDeclStmt(identifier);
        }

        this.expectSemicolon(ctx);
        return varDeclStatement;
    }

    // ===== KEYWORD STATEMENTS =====

    private Stmt printStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.PRINT);
        Expr expression = this.expression(ctx);
        Stmt printStmt = new Stmt.PrintStmt(expression);
        this.expectSemicolon(ctx);
        return printStmt;
    }

    private Stmt breakStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.BREAK);
        Stmt breakStatement = new Stmt.SingleKeywordStmt(ctx.getLastMatchedToken());
        this.expectSemicolon(ctx);
        return breakStatement;
    }

    private Stmt continueStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.CONTINUE);
        Stmt continueStatement = new Stmt.SingleKeywordStmt(ctx.getLastMatchedToken());
        this.expectSemicolon(ctx);
        return continueStatement;
    }

    // ===== BLOCK STATEMENT =====

    private Stmt blockStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.LEFT_BRACE);
        List<Stmt> blockDeclarations = new ArrayList<>();
        while (!ctx.lookahead(LoxTokenType.RIGHT_BRACE)) {
            blockDeclarations.add(this.declaration(ctx));
        }
        ctx.match(LoxTokenType.RIGHT_BRACE);

        return new Stmt.BlockStmt(blockDeclarations);
    }

    // ===== EXPRESSION STATEMENT =====

    private Stmt expressionStatement (LoxParser.Context ctx) {
        Expr expression = this.expression(ctx);
        Stmt expressionStmt = new Stmt.ExpressionStmt(expression);
        this.expectSemicolon(ctx);
        return expressionStmt;
    }

    // ===== EXPRESSIONS =====

    private Expr expression (LoxParser.Context ctx) {
        return assignment(ctx);
    }

    private Expr assignment (LoxParser.Context ctx) {
        if (ctx.kLookahead(LoxTokenType.IDENTIFIER, LoxTokenType.EQUAL)) {
            // expecting an assignment
            ctx.match(LoxTokenType.IDENTIFIER);
            LoxToken identifier = ctx.getLastMatchedToken();
            ctx.match(LoxTokenType.EQUAL);
            return new Expr.Assignment(identifier, assignment(ctx));
        } else {
            // expecting logicOr
            return logicOr(ctx);
        }
    }

    private Expr logicOr (LoxParser.Context ctx) {
        Expr expr = logicAnd(ctx);
        while (ctx.match(LoxTokenType.AND)) {
            LoxToken operator = ctx.getLastMatchedToken();
            Expr right = logicAnd(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr logicAnd (LoxParser.Context ctx) {
        Expr expr = equality(ctx);
        while (ctx.match(LoxTokenType.OR)) {
            LoxToken operator = ctx.getLastMatchedToken();
            Expr right = equality(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    
    private Expr equality (LoxParser.Context ctx) {
        Expr expr = comparison(ctx);
        while (ctx.match(LoxTokenType.BANG_EQUAL, LoxTokenType.EQUAL_EQUAL)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = comparison(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }   

    private Expr comparison (LoxParser.Context ctx) {
        Expr expr = term(ctx);
        while (ctx.match(
            LoxTokenType.LESS, 
            LoxTokenType.GREATER, 
            LoxTokenType.LESS_EQUAL, 
            LoxTokenType.GREATER_EQUAL
        )) {
            var operator = ctx.getLastMatchedToken();
            Expr right = term(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term (LoxParser.Context ctx) {
        Expr expr = factor(ctx);
        while (ctx.match(LoxTokenType.MINUS, LoxTokenType.PLUS)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = factor(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor (LoxParser.Context ctx) {
        Expr expr = unary(ctx);
        while (ctx.match(LoxTokenType.STAR, LoxTokenType.SLASH)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = unary(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary (LoxParser.Context ctx) {
        if (ctx.match(LoxTokenType.MINUS, LoxTokenType.BANG)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = unary(ctx);
            return new Expr.Unary(operator, right);
        } else {
            return call(ctx);
        }
    }

    private Expr call (LoxParser.Context ctx) {
        Expr expr = primary(ctx);
        while (ctx.match(LoxTokenType.LEFT_PAREN)) {
            List<Expr> arguments = argument(ctx);
            ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected ')' after call arguments");
            expr = new Expr.Call(expr, ctx.getLastMatchedToken(), arguments);
        }

        return expr;
    }

    private List<Expr> argument (LoxParser.Context ctx) {
        List<Expr> arguments = new ArrayList<>();
        if (!ctx.match(LoxTokenType.RIGHT_PAREN)) {
            do { 
                if (arguments.size() >= 255) {
                    ctx.error(ctx.getLastMatchedToken(), "cannot have more than 255 arguments");
                }
                arguments.add(expression(ctx));
            } while (ctx.match(LoxTokenType.COMMA));
        }

        return arguments;
    }

    private Expr primary (LoxParser.Context ctx) {
        if (ctx.match(
            LoxTokenType.NUMBER,
            LoxTokenType.STRING,
            LoxTokenType.TRUE,
            LoxTokenType.FALSE,
            LoxTokenType.NIL
        )) {
            return new Expr.Literal(ctx.getLastMatchedToken()); 
        } else if (ctx.match(LoxTokenType.IDENTIFIER)) {
            return new Expr.Variable(ctx.getLastMatchedToken());
        } else if (ctx.match(LoxTokenType.LEFT_PAREN)) {
            Expr expression = expression(ctx);
            ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected closing \")\" after a nested expression");
            return new Expr.Grouping(expression);
        }

        throw ctx.error(ctx.getLastMatchedToken(), "expected an expression");
    }
}
