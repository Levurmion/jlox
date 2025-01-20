package com.lox.parser.ast;

import java.util.ArrayList;
import java.util.List;

import com.lox.lexer.LoxToken;

public abstract class Stmt {
    public static interface Visitor<R> {
        public R visitVarDeclStmt (Stmt.VarDeclStmt stmt);
        public R visitFunDeclStmt (Stmt.FunDeclStmt stmt);
        public R visitExpressionStmt (Stmt.ExpressionStmt stmt);
        public R visitPrintStmt (Stmt.PrintStmt stmt);
        public R visitReturnStmt (Stmt.ReturnStmt stmt);
        public R visitBlockStmt (Stmt.BlockStmt stmt);
        public R visitIfStmt (Stmt.IfStmt stmt);
        public R visitWhileStmt (Stmt.WhileStmt stmt);
        public R visitSingleKeywordStmt (Stmt.SingleKeywordStmt stmt);
    }

    public abstract <R> R accept (Stmt.Visitor<R> visitor);

    public static class VarDeclStmt extends Stmt {
        final public LoxToken identifier;
        final public Expr expression;

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

    public static class FunDeclStmt extends Stmt {
        final public LoxToken identifier;
        final public List<LoxToken> parameters;
        final public Stmt.BlockStmt body;

        public FunDeclStmt(LoxToken identifier, List<LoxToken> parameters, Stmt.BlockStmt body) {
            this.identifier = identifier;
            this.parameters = parameters;
            this.body = body;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitFunDeclStmt(this);
        }

        @Override
        public String toString () {
            return "( FUN " + this.identifier.lexeme + " PARAMS " + this.parameters.toString() + " )";
        }

        
    }

    public static class WhileStmt extends Stmt {
        final public Expr condition;
        final public Stmt statement;

        public WhileStmt(Expr condition, Stmt statement) {
            this.condition = condition;
            this.statement = statement;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        @Override
        public String toString () {
            return "(\n\tWHILE " + this.condition.toString() + "\n\tTHEN " + this.statement.toString() + "\n)";
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

    public static class ReturnStmt extends Stmt {
        final public Expr expression;

        public ReturnStmt(Expr expression) {
            this.expression = expression;
        }

        public ReturnStmt() {
            this.expression = null;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        @Override
        public String toString () {
            return "RETURN: " + this.expression.toString();
        }
    }

    public static class SingleKeywordStmt extends Stmt {
        final public LoxToken keyword;

        public SingleKeywordStmt(LoxToken keyword) {
            this.keyword = keyword;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitSingleKeywordStmt(this);
        }

        @Override 
        public String toString () {
            return "( " + this.keyword.lexeme + " )"; 
        }
    }

    public static class IfStmt extends Stmt {
        final public Expr condition;
        final public Stmt statement;
        final public List<Stmt.IfStmt> elseIfStatements;
        final public Stmt elseStatement;

        public IfStmt(Expr condition, Stmt statement, List<Stmt.IfStmt> elseIfStatement, Stmt elseStatement) {
            this.condition = condition;
            this.statement = statement;
            this.elseIfStatements = elseIfStatement;
            this.elseStatement = elseStatement;
        }

        public IfStmt(Expr condition, Stmt statement) {
            this.condition = condition;
            this.statement = statement;
            this.elseIfStatements = new ArrayList<>();
            this.elseStatement = null;
        }

        @Override
        public <R> R accept (Stmt.Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        @Override
        public String toString() {
            String asString = "( IF " + this.condition.toString() + " THEN " + this.statement.toString();
            for (var elseIfStatement : this.elseIfStatements) {
                asString += "\n" + elseIfStatement.toString();
            }
            if (this.elseStatement != null) {
                asString += "\nELSE " + this.elseStatement.toString() + " )";
            }

            return asString;
        }
        
    }
}
