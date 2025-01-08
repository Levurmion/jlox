package com.lox.parser;

import java.util.ArrayList;
import java.util.List;

import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.Stmt;

public class LoxGrammar {

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
            stmt = this.varDeclStmt(ctx);
        } else {
            stmt = this.statement(ctx);
        }

        if (stmt == null) {
            throw ctx.error(ctx.getLastMatchedToken(), "invalid declaration");
        } else {
            ctx.matchOrThrow(LoxTokenType.SEMICOLON, "expected ';' after statements");
            return stmt;
        }
    }

    private Stmt statement (LoxParser.Context ctx) {
        if (ctx.lookahead(LoxTokenType.PRINT)) {
            return this.printStatement(ctx);
        } else {
            return this.expressionStatement(ctx);
        }
    }

    private Stmt varDeclStmt (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.VAR);
        ctx.matchOrThrow(LoxTokenType.IDENTIFIER, "expected variable name");
        LoxToken identifier = ctx.getLastMatchedToken();
        if (ctx.match(LoxTokenType.EQUAL)) {
            // expecting a right-hand expression
            Expr expr = this.expression(ctx);
            return new Stmt.VarDeclStmt(identifier, expr);
        } else {
            // an uninitialized variable declaration
            return new Stmt.VarDeclStmt(identifier);
        }
    }

    private Stmt expressionStatement (LoxParser.Context ctx) {
        Expr expression = this.expression(ctx);

        return new Stmt.ExpressionStmt(expression);
    }

    private Stmt printStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.PRINT);
        Expr expression = this.expression(ctx);

        return new Stmt.PrintStmt(expression);
    }

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
            // expecting equality
            return equality(ctx);
        }
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
            return primary(ctx);
        }
    }

    private Expr primary (LoxParser.Context ctx) {
        if (ctx.match(
            LoxTokenType.NUMBER,
            LoxTokenType.STRING,
            LoxTokenType.TRUE,
            LoxTokenType.FALSE,
            LoxTokenType.NIL,
            LoxTokenType.IDENTIFIER
        )) {
            return new Expr.Literal(ctx.getLastMatchedToken()); 

        } else if (ctx.match(LoxTokenType.LEFT_PAREN)) {
            Expr expression = expression(ctx);
            ctx.matchOrThrow(LoxTokenType.RIGHT_PAREN, "expected closing \")\" after a nested expression");
            return new Expr.Grouping(expression);
        }

        throw ctx.error(ctx.getCurrToken(), "expected an expression");
    }
}
