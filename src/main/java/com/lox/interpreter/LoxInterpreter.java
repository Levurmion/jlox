package com.lox.interpreter;

import java.util.HashMap;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.interpreter.helpers.ExprHelper;
import com.lox.lexer.LoxLexer;
import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.Stmt;

public class LoxInterpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private LoxLexer lexer;
    private LoxParser parser;

    // constants
    public static final String UNINITIALIZED = "UNINITIALIZED";

    // interpreter states
    final private HashMap<String, Object> variables = new HashMap<>();
    
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
        String identifier = (String)varDeclStmt.identifier.literal;
        Expr expression = varDeclStmt.expression;
        if (expression == null) {
            this.variables.put(identifier, LoxInterpreter.UNINITIALIZED);
        } else {
            this.variables.put(identifier, this.evaluate(expression));
        }

        return null;
    }

    @Override
    public Void visitVarReassignmentStmt (Stmt.VarReassignmentStmt varReassignmentStmt) {
        String identifier = (String)varReassignmentStmt.identifier.literal;
        Expr expression = varReassignmentStmt.expression;
        if (this.variables.containsKey(identifier)) {
            this.variables.put(identifier, this.evaluate(expression));
        } else {
            throw new RuntimeError(varReassignmentStmt.identifier, "undeclared variable");
        }

        return null;
    }

    @Override
    public Void visitExpressionStmt (Stmt.ExpressionStmt exprStmt) {
        return null;
    }

    @Override
    public Void visitPrintStmt (Stmt.PrintStmt printStmt) {
        Object expressionValue = this.evaluate(printStmt.expression);
        System.out.println(expressionValue);
        return null;
    }

    // ===== EXPRESSION VISITOR METHODS =====

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
            case MINUS:{
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
        if (literal.token.type == LoxTokenType.IDENTIFIER) {
            String varName = (String)literal.token.literal;
            if (this.variables.containsKey(varName)) {
                Object varValue = this.variables.get(varName);
                if (varValue == LoxInterpreter.UNINITIALIZED) {
                    throw new RuntimeError(literal.token, "cannot access uninitialized variable");
                } else {
                    return varValue;
                }
            } else {
                throw new RuntimeError(literal.token, "undeclared variable");
            }
        }
        
        return literal.token.literal;
    }
}
