package com.lox.parser.ast;

import com.lox.interfaces.AstNodeInterface;
import com.lox.interpreter.AstVisitor;
import com.lox.lexer.LoxToken;

public abstract class Expr implements AstNodeInterface {

    public static class Grouping extends Expr {
        final public Expr expression;

        public Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        public Object accept (AstVisitor visitor) {
            return visitor.visitGroupingExpr(this);
        }

        @Override
        public String toString () {
            return this.expression.toString();
        }
    }
    
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
        public Object accept (AstVisitor visitor) {
            return visitor.visitBinaryExpr(this);
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
        public Object accept (AstVisitor visitor) {
            return visitor.visitUnaryExpr(this);
        }

        @Override
        public String toString () {
            return ("{ operator: " + this.operator.literal + ", right: " + this.right + "}");
        }
    }

    public static class Literal extends Expr {
        final public LoxToken token;

        public Literal(LoxToken token) {
            this.token = token;
        }

        @Override
        public Object accept (AstVisitor visitor) {
            return visitor.visitLiteralExpr(this);
        } 

        @Override
        public String toString () {
            return this.token.lexeme;
        }
    }
}
