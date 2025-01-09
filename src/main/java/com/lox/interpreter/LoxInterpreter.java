package com.lox.interpreter;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.interpreter.helpers.ExprHelper;
import com.lox.lexer.LoxLexer;
import com.lox.lexer.LoxToken;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.Stmt;

public class LoxInterpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private LoxLexer lexer;
    private LoxParser parser;

    // interpreter states
    private Environment environment;

    public LoxInterpreter () {
        this.environment = new Environment();
    }
    
    public void interpret (String source) {
        this.lexer = new LoxLexer(source);
        this.lexer.tokenize();
        this.parser = new LoxParser(this.lexer.tokens, new LoxGrammar());
        this.parser.parse();

        for (var statement : this.parser.program) {
            this.execute(statement);
        }
    }

    private Void execute (Stmt statement) {
        statement.accept(this);
        return null;
    }

    private Object evaluate (Expr expression) {
        return expression.accept(this);
    }

    // ===== STATEMENT VISITOR METHODS =====

    @Override
    public Void visitVarDeclStmt (Stmt.VarDeclStmt varDeclStmt) {
        Expr expression = varDeclStmt.expression;
        if (expression == null) {
            this.environment.define(varDeclStmt.identifier);
        } else {
            this.environment.define(varDeclStmt.identifier, this.evaluate(expression));
        }
        return null;
    }

    @Override
    public Void visitBlockStmt (Stmt.BlockStmt blockStmt) {
        // create a inner scope
        Environment blockScope = new Environment(this.environment);
        Environment outerScope = this.environment;
        this.environment = blockScope;

        try {
            // execute block declarations
            for (var declaration : blockStmt.declarations) {
                this.execute(declaration);
            }
        } finally {
            // delete inner scope
            this.environment = outerScope;
        }

        return null;
    }
    
    @Override
    public Void visitExpressionStmt (Stmt.ExpressionStmt exprStmt) {
        this.evaluate(exprStmt.expression);
        return null;
    }
    
    @Override
    public Void visitPrintStmt (Stmt.PrintStmt printStmt) {
        Object expressionValue = this.evaluate(printStmt.expression);
        System.out.println(expressionValue);
        return null;
    }

    @Override
    public Void visitIfStmt (Stmt.IfStmt ifStmt) {
        if (ExprHelper.isTruthy(this.evaluate(ifStmt.condition))) {
            return this.execute(ifStmt.statement);
        }

        for (var elseIf : ifStmt.elseIfStatements) {
            if (ExprHelper.isTruthy(this.evaluate(elseIf.condition))) {
                return this.execute(elseIf.statement);
            }
        }
        
        if (ifStmt.elseStatement != null) {
            return this.execute(ifStmt.elseStatement);
        }

        return null;
    }

    @Override
    public Void visitWhileStmt (Stmt.WhileStmt whileStmt) {
        while (ExprHelper.isTruthy(this.evaluate(whileStmt.condition))) {
            this.execute(whileStmt.statement);
        }
        return null;
    }
    
    // ===== EXPRESSION VISITOR METHODS =====

    @Override
    public Object visitAssignmentExpr (Expr.Assignment assignment) {
        Expr right = assignment.right;
        Object rightValue = this.evaluate(right);
        this.environment.assign(assignment.identifier, rightValue);

        return rightValue;
    }
    
    @Override
    public Object visitGroupingExpr (Expr.Grouping group) {
        return this.evaluate(group.expression);
    }
    
    @Override
    public Object visitBinaryExpr (Expr.Binary binary) {
        Object left = this.evaluate(binary.left);
        Object right = this.evaluate(binary.right);
        LoxToken operator = binary.operator;

        Object result = null;
        switch (operator.type) {
            case PLUS: {
                if (ExprHelper.isString(left, right)) {
                    result = (String)left + (String)right;
                } else if (ExprHelper.isNumber(left, right)) {
                    result = (Double)left + (Double)right;
                } else {
                    throw new RuntimeError(operator, "operands must be of type: 'String' or 'Number'");
                }
                break;
            }
            case MINUS: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left - (Double)right;
                break;
            }
            case STAR: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left * (Double)right;
                break;
            }
            case SLASH: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left / (Double)right;
                break;
            }
            case LESS: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left < (Double)right;
                break;
            }
            case GREATER: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left > (Double)right;
                break;
            }
            case LESS_EQUAL: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left <= (Double)right;
                break;
            }
            case GREATER_EQUAL: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left >= (Double)right;
                break;
            }
            case EQUAL_EQUAL: {
                result = ExprHelper.isEqual(left, right);
                break;
            }
            case BANG_EQUAL: {
                result = !ExprHelper.isEqual(left, right);
                break;
            }
            case AND: {
                result = ExprHelper.isTruthy(left) && ExprHelper.isTruthy(right);
                break;
            }
            case OR: {
                result = ExprHelper.isTruthy(left) || ExprHelper.isTruthy(right);
                break;
            }
            default: {
                throw new RuntimeError(operator, "invalid binary operator");
            }
        }

        return result;
    }

    @Override
    public Object visitUnaryExpr (Expr.Unary unary) {
        LoxToken operator = unary.operator;
        Object right = this.evaluate(unary.right);

        Object result = null;
        switch (operator.type) {
            case MINUS:
                ExprHelper.assertNumberOperand(operator, right);
                result = -(Double)right;                
                break;
            case BANG:
                result = !(ExprHelper.isTruthy(right));
                break;
            default:
                throw new RuntimeError(operator, "invalid unary operator");
        }

        return result;
    }

    @Override
    public Object visitLiteralExpr (Expr.Literal literal) {
        return literal.token.literal;
    }

    @Override
    public Object visitVariableExpr (Expr.Variable variable) {
        return this.environment.use(variable.token);
    }
}
