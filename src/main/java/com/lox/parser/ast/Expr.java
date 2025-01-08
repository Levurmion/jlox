package com.lox.parser.ast;

import com.lox.lexer.LoxToken;

public abstract class Expr {
    public static interface Visitor<R> {
        public R visitAssignmentExpr(Expr.Assignment expr);
        public R visitGroupingExpr(Expr.Grouping expr);
        public R visitBinaryExpr(Expr.Binary expr);
        public R visitUnaryExpr(Expr.Unary expr);
        public R visitLiteralExpr(Expr.Literal expr);
    }

    public abstract <R> R accept (Expr.Visitor<R> visitor);

    public static class Assignment extends Expr {
        final public LoxToken identifier;
        final public Expr right;

        public Assignment(LoxToken identifier, Expr right) {
            this.identifier = identifier;
            this.right = right;
        }

        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitAssignmentExpr(this);
        }

        @Override
        public String toString () {
            return ("{ assign_id: " + this.identifier.literal + ", right: " + this.right.toString() + " }");
        }
    }

    public static class Grouping extends Expr {
        final public Expr expression;

        public Grouping(Expr expression) {
            this.expression = expression;
        }
        
        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
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
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        @Override
        public String toString () {
            return ("{ left: " + this.left + ", operator: " + this.operator.literal + ", right: " + this.right + " }");
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
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        @Override
        public String toString () {
            return ("{ operator: " + this.operator.literal + ", right: " + this.right + " }");
        }
    }

    public static class Literal extends Expr {
        final public LoxToken token;

        public Literal(LoxToken token) {
            this.token = token;
        }
        
        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        } 

        @Override
        public String toString () {
            return this.token.lexeme;
        }
    }
}
