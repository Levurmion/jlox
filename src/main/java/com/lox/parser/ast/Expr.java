package com.lox.parser.ast;

import com.lox.lexer.LoxToken;

public abstract class Expr extends AstNode {
    
    public static class Binary extends Expr {
        final public Expr left;
        final public Expr right;
        final public LoxToken operator;

        public Binary(Expr left, LoxToken operator, Expr right) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public String toString () {
            return ("{ left: " + this.left + ", operator: " + this.operator.literal + ", right: " + this.right + "}");
        }
    }

    public static class Unary extends Expr {
        final public LoxToken operator;
        final public Expr right;

        public Unary(LoxToken operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public String toString () {
            return ("{ operator: " + this.operator.literal + ", right: " + this.right + "}");
        }
    }

    public static class Literal extends Expr {
        final public LoxToken literal;

        public Literal(LoxToken literal) {
            this.literal = literal;
        }

        @Override
        public String toString () {
            return this.literal.lexeme;
        }
    }
}
