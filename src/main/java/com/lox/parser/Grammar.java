package com.lox.parser;

import com.lox.lexer.lox.LoxTokenType;
import com.lox.parser.ast.AstNode;
import com.lox.parser.ast.Expr;
import com.lox.parser.exceptions.ParsingException;

public class Grammar {

    public AstNode start (Parser.Context ctx) throws ParsingException {
        return expression(ctx);
    }

    private Expr expression (Parser.Context ctx) throws ParsingException {
        return equality(ctx);
    }
    
    private Expr equality (Parser.Context ctx) throws ParsingException {
        Expr expr = comparison(ctx);
        while (ctx.match(LoxTokenType.BANG_EQUAL, LoxTokenType.EQUAL_EQUAL)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = comparison(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }   

    private Expr comparison (Parser.Context ctx) throws ParsingException {
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

    private Expr term (Parser.Context ctx) throws ParsingException {
        Expr expr = factor(ctx);
        while (ctx.match(LoxTokenType.MINUS, LoxTokenType.PLUS)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = factor(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor (Parser.Context ctx) throws ParsingException {
        Expr expr = unary(ctx);
        while (ctx.match(LoxTokenType.STAR, LoxTokenType.SLASH)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = unary(ctx);
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary (Parser.Context ctx) throws ParsingException {
        if (ctx.match(LoxTokenType.MINUS, LoxTokenType.BANG)) {
            var operator = ctx.getLastMatchedToken();
            Expr right = unary(ctx);
            return new Expr.Unary(operator, right);
        } else {
            return primary(ctx);
        }
    }

    private Expr primary (Parser.Context ctx) throws ParsingException {
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
            
            if (ctx.match(LoxTokenType.RIGHT_PAREN)) {
                return expression;
            } else {
                throw new ParsingException(ctx.getCurrToken());
            }
        }

        throw new ParsingException(ctx.getCurrToken());
    }
}
