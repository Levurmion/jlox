package com.lox.interpreter;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.interpreter.helpers.ExprHelper;
import com.lox.lexer.LoxLexer;
import com.lox.lexer.LoxToken;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;
import com.lox.parser.ast.Expr;

public class LoxInterpreter implements Expr.Visitor<Object> {
    private LoxLexer lexer;
    private LoxParser parser;
    
    public Object interpret (String source) {
        this.lexer = new LoxLexer(source);
        this.lexer.tokenize();

        this.parser = new LoxParser(this.lexer.tokens, new LoxGrammar());
        this.parser.parse();

        return this.evaluate(this.parser.ast);
    }

    private Object evaluate (Expr expression) {
        return expression.accept(this);
    }

    // ===== VISITOR METHODS =====

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
        return literal.token.literal;
    }
}
