package com.lox.parser.ast;

import com.lox.lexer.lox.LoxToken;

public abstract class Expr extends AstNode {
    
    static class Binary extends Expr {
        final public Expr left;
        final public Expr right;
        final public LoxToken operator;

        public Binary(Expr left, LoxToken operator, Expr right) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }
    }

    static class Unary extends Expr {
        final public LoxToken operator;
        final public Expr right;

        public Unary(LoxToken operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }

    static class Literal extends Expr {
        final public Object literal;

        public Literal(Object literal) {
            this.literal = literal;
        }
    }
}
