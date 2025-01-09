package com.lox.parser.ast;

import java.util.List;

import com.lox.lexer.LoxToken;

public abstract class Stmt {
    public static interface Visitor<R> {
        public R visitVarDeclStmt (Stmt.VarDeclStmt stmt);
        public R visitExpressionStmt (Stmt.ExpressionStmt stmt);
        public R visitPrintStmt (Stmt.PrintStmt stmt);
        public R visitBlockStmt (Stmt.BlockStmt stmt);
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

        @Override
        public String toString () {
            if (this.expression != null) {
                return "( VAR_DECL: " + this.identifier.lexeme + ", expr: " + this.expression.toString() + " )";
            } else {
                return "( VAR_DECL: " + this.identifier.lexeme + ", expr: null )";
            }
        }
    }

    public static class BlockStmt extends Stmt {
        final public List<Stmt> declarations;

        public BlockStmt (List<Stmt> declarations) {
            this.declarations = declarations;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        @Override
        public String toString () {
            return "{ " + this.declarations.toString() + " }";
        }
    }

    public static class ExpressionStmt extends Stmt {
        final public Expr expression;

        public ExpressionStmt (Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        @Override
        public String toString () {
            return "( EXPR_STMT: " + this.expression.toString() + " )";
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

        @Override
        public String toString () {
            return "( PRINT: " + this.expression.toString() + " )";
        }
    }
}
