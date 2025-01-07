package com.lox.parser.ast;

public abstract class Stmt {
    public static interface Visitor<R> {
        public R visitStmt (Stmt stmt);
        public R visitExpressionStmt (Stmt stmt);
        public R visitPrintStmt (Stmt stmt);
    }

    public abstract <R> R accept (Stmt.Visitor<R> visitor);

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
