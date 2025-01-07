package com.lox.parser;

import java.util.ArrayList;
import java.util.List;

import com.lox.lexer.LoxTokenType;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.Stmt;

public class LoxGrammar {

    public List<Stmt> start (LoxParser.Context ctx) {
        List<Stmt> statements = new ArrayList<>();
        while (!ctx.isAtEnd()) {
            statements.add(this.statement(ctx));
        }
        
        return statements;
    }

    private Stmt statement (LoxParser.Context ctx) {
        if (ctx.lookahead(LoxTokenType.PRINT)) {
            return this.printStatement(ctx);
        } else {
            return this.expressionStatement(ctx);
        }
    }

    private Stmt expressionStatement (LoxParser.Context ctx) {
        Expr expression = this.expression(ctx);
        ctx.matchOrThrow(ctx.getCurrToken().type, "expected ';' after a statement");

        return new Stmt.ExpressionStmt(expression);
    }

    private Stmt printStatement (LoxParser.Context ctx) {
        ctx.match(LoxTokenType.PRINT);
        Expr expression = this.expression(ctx);
        ctx.matchOrThrow(ctx.getCurrToken().type, "expected ';' after a statement");

        return new Stmt.PrintStmt(expression);
    }

    private Expr expression (LoxParser.Context ctx) {
        return equality(ctx);
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
            LoxTokenType.NIL
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
