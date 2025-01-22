package com.lox.parser.ast;

import java.util.List;

import com.lox.lexer.LoxToken;

public abstract class Expr {
    public static interface Visitor<R> {
        public R visitAssignmentExpr(Expr.Assignment expr);
        public R visitAnonymousFuncExpr(Expr.AnonymousFunc expr);
        public R visitGroupingExpr(Expr.Grouping expr);
        public R visitBinaryExpr(Expr.Binary expr);
        public R visitUnaryExpr(Expr.Unary expr);
        public R visitLiteralExpr(Expr.Literal expr);
        public R visitVariableExpr(Expr.Variable expr);
        public R visitCallExpr(Expr.Call expr);
    }

    public abstract <R> R accept (Expr.Visitor<R> visitor);

    public static class Assignment extends Expr {
        final public LoxToken variable;
        final public Expr right;

        public Assignment(LoxToken variable, Expr right) {
            this.variable = variable;
            this.right = right;
        }

        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitAssignmentExpr(this);
        }

        @Override
        public String toString () {
            return ("{ assign_id: " + this.variable.literal + ", right: " + this.right.toString() + " }");
        }
    }

    public static class AnonymousFunc extends Expr {
        final public List<LoxToken> parameters;
        final public Stmt.BlockStmt body;

        public AnonymousFunc(List<LoxToken> parameters, Stmt.BlockStmt body) {
            this.parameters = parameters;
            this.body = body;
        }

        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitAnonymousFuncExpr(this);
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

    public static class Variable extends Expr {
        final public LoxToken token;

        public Variable(LoxToken token) {
            this.token = token;
        }

        @Override 
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        @Override
        public String toString () {
            return this.token.lexeme;
        }
    }

    public static class Call extends Expr {
        final public Expr callee;
        final public LoxToken paren;
        final public List<Expr> arguments;

        public Call(Expr callee, LoxToken paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        public <R> R accept (Expr.Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        @Override
        public String toString () {
            return "( CALL " + this.callee.toString() + " ARGS " + this.arguments.toString() + " )"; 
        }
    }
}
