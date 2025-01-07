package com.lox.interpreter;

import com.lox.interfaces.VisitorInterface;
import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.lexer.LoxToken;
import com.lox.parser.ast.Expr;

public class AstVisitor implements VisitorInterface {
    
    // ===== HELPER METHODS =====

    private boolean isTruthy (Object obj) {
        if (obj == null) return false;
        else if (obj instanceof Boolean) return (boolean)obj;
        return true;
    }

    private boolean isEqual (Object left, Object right) {
        if (left == null && right == null) return true;
        else if (left == null) return false;

        return left.equals(right);
    }

    private boolean isString (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof String)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumber (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof Double)) {
                return false;
            }
        }
        return true;
    }


    private Object evaluate (Expr expression) {
        return expression.accept(this);
    }

    // ===== RUNTIME TYPE GUARDS =====
    private boolean assertNumberOperands (LoxToken operator, Object left, Object right) {
        if (!this.isNumber(left, right)) {
            throw new RuntimeError(operator, "operands must be of type: 'Number'");
        }
        return true;
    }

    private boolean assertNumberOperand (LoxToken operator, Object right) {
        if (!this.isNumber(right)) {
            throw new RuntimeError(operator, "operand must be of type: 'Number'");
        }
        return true;
    }

    // ===== VISITOR METHODS =====

    public Object visitGroupingExpr (Expr.Grouping group) {
        return this.evaluate(group.expression);
    }
    
    public Object visitBinaryExpr (Expr.Binary binary) {
        Object left = this.evaluate(binary.left);
        Object right = this.evaluate(binary.right);
        LoxToken operator = binary.operator;

        Object result = null;
        switch (operator.type) {
            case PLUS: {
                if (this.isString(left, right)) {
                    result = (String)left + (String)right;
                } else if (this.isNumber(left, right)) {
                    result = (Double)left + (Double)right;
                } else {
                    throw new RuntimeError(operator, "operands must be of type: 'String' or 'Number'");
                }
                break;
            }
            case MINUS:{
                this.assertNumberOperands(operator, left, right);
                result = (Double)left - (Double)right;
                break;
            }
            case STAR: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left * (Double)right;
                break;
            }
            case SLASH: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left / (Double)right;
                break;
            }
            case LESS: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left < (Double)right;
                break;
            }
            case GREATER: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left > (Double)right;
                break;
            }
            case LESS_EQUAL: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left <= (Double)right;
                break;
            }
            case GREATER_EQUAL: {
                this.assertNumberOperands(operator, left, right);
                result = (Double)left >= (Double)right;
                break;
            }
            case EQUAL_EQUAL: {
                result = this.isEqual(left, right);
                break;
            }
            case BANG_EQUAL: {
                result = !this.isEqual(left, right);
                break;
            }
            default: {
                throw new RuntimeError(operator, "invalid binary operator");
            }
        }

        return result;
    }

    public Object visitUnaryExpr (Expr.Unary unary) {
        LoxToken operator = unary.operator;
        Object right = this.evaluate(unary.right);

        Object result = null;
        switch (operator.type) {
            case MINUS:
                this.assertNumberOperand(operator, right);
                result = -(Double)right;                
                break;
            case BANG:
                result = !(this.isTruthy(right));
                break;
            default:
                throw new RuntimeError(operator, "invalid unary operator");
        }

        return result;
    }

    public Object visitLiteralExpr (Expr.Literal literal) {
        return literal.token.literal;
    }
}
