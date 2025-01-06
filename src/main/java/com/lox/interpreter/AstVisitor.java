package com.lox.interpreter;

import com.lox.Lox;
import com.lox.interfaces.VisitorInterface;
import com.lox.interpreter.exceptions.SyntaxError;
import com.lox.lexer.LoxToken;
import com.lox.parser.ast.Expr;
import com.lox.parser.exceptions.ParseError;

public class AstVisitor implements VisitorInterface {

    private boolean isString (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof String)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDouble (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof Double)) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoolean (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof Boolean)) {
                return false;
            }
        }
        return true;
    }

    private Object evaluate (Expr expression) {
        return expression.accept(this);
    }

    public Object visitGroupingExpr (Expr.Grouping group) {
        return this.evaluate(group.expression);
    }
    
    public Object visitBinaryExpr (Expr.Binary binary) {
        Object left = this.evaluate(binary.left);
        Object right = this.evaluate(binary.right);
        LoxToken operator = binary.operator;

        Object result = null;
        switch (operator.type) {
            case PLUS:
                if (this.isString(left, right)) {
                    result = (String)left + (String)right;
                } else if (this.isDouble(left, right)) {
                    result = (Double)left + (Double)right;
                }
                break;
            case MINUS:
                if (this.isDouble(left, right)) {
                    result = (Double)left - (Double)right;
                }
                break;
            case STAR:
                if (this.isDouble(left, right)) {
                    result = (Double)left * (Double)right;
                }
                break;
            case SLASH:
                if (this.isDouble(left, right)) {
                    result = (Double)left / (Double)right;
                }
                break;
            case EQUAL_EQUAL:
                if (this.isDouble(left, right)) {
                    result = ((Double)left).equals((Double)right);
                } else if (this.isString(left, right)) {
                    result = ((String)left).equals((String)right);
                } else if (this.isBoolean(left, right)) {
                    result = ((Boolean)left).equals((Boolean)right);
                } else {
                    result = left == right;
                }
                break;
            case BANG_EQUAL:
            if (this.isDouble(left, right)) {
                result = !((Double)left).equals((Double)right);
            } else if (this.isString(left, right)) {
                result = !((String)left).equals((String)right);
            } else if (this.isBoolean(left, right)) {
                result = !((Boolean)left).equals((Boolean)right);
            } else {
                result = left == right;
            }
                break;
            default:
                Lox.error(operator, "invalid binary operator");
        }

        if (result == null) {
            Lox.error(operator, "left (" + left +") and right (" + right + ") operands are incompatible");
            throw new SyntaxError();
        }

        return result;
    }

    public Object visitUnaryExpr (Expr.Unary unary) {
        LoxToken operator = unary.operator;
        Object right = this.evaluate(unary.right);

        Object result = null;
        switch (operator.type) {
            case MINUS:
                if (this.isDouble(right)) {
                    result = -(Double)right;
                }
                break;
            case BANG:
                if (this.isBoolean(right)) {
                    result = !(boolean)right;
                }
                break;
            default:
                Lox.error(operator, "invalid unary operator");
        }

        if (result == null) {
            Lox.error(operator, "unary operator cannot act on datatype" + right.getClass().getName());
            throw new ParseError();
        }

        return result;
    }

    public Object visitLiteralExpr (Expr.Literal literal) {
        return literal.token.literal;
    }
}
