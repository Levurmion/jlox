package com.lox.parser.ast;

import com.lox.lexer.LoxToken;

public abstract class Stmt {
    public static interface Visitor<R> {
        public R visitVarDeclStmt (Stmt.VarDeclStmt stmt);
        public R visitVarReassignmentStmt (Stmt.VarReassignmentStmt stmt);
        public R visitExpressionStmt (Stmt.ExpressionStmt stmt);
        public R visitPrintStmt (Stmt.PrintStmt stmt);
    }

    public abstract <R> R accept (Stmt.Visitor<R> visitor);

    public static class VarDeclStmt extends Stmt {
        final public LoxToken identifier;
        public Expr expression;

        public VarDeclStmt(LoxToken identifier) {
            this.identifier = identifier;
            this.expression = null;
        }

        public VarDeclStmt(LoxToken identifier, Expr expression) {
            this.identifier = identifier;
            this.expression = expression;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitVarDeclStmt(this);
        }   
    }

    public static class VarReassignmentStmt extends Stmt {
        final public LoxToken identifier;
        final public Expr expression;

        public VarReassignmentStmt(LoxToken identifier, Expr expression) {
            this.identifier = identifier;
            this.expression = expression;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitVarReassignmentStmt(this);
        }
    }

    public static class ExpressionStmt extends Stmt {
        final public Expr expression;

        public ExpressionStmt(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    public static class PrintStmt extends Stmt {
        final public Expr expression;

        public PrintStmt(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }
}
