package com.lox.interpreter.helpers;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.lexer.LoxToken;

public class ExprHelper {

    // ===== HELPER METHODS =====

    public static boolean isTruthy (Object obj) {
        if (obj == null) return false;
        else if (obj instanceof Boolean) return (boolean)obj;
        return true;
    }

    public static boolean isEqual (Object left, Object right) {
        if (left == null && right == null) return true;
        else if (left == null) return false;

        return left.equals(right);
    }

    public static boolean isString (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof String)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber (Object... objs) {
        for (var obj : objs) {
            if (!(obj instanceof Double)) {
                return false;
            }
        }
        return true;
    }
    
    // ===== RUNTIME TYPE GUARDS =====
    
    public static boolean assertNumberOperands (LoxToken operator, Object left, Object right) {
        if (!ExprHelper.isNumber(left, right)) {
            throw new RuntimeError(operator, "operands must be of type: 'Number'");
        }
        return true;
    }

    public static boolean assertNumberOperand (LoxToken operator, Object right) {
        if (!ExprHelper.isNumber(right)) {
            throw new RuntimeError(operator, "operand must be of type: 'Number'");
        }
        return true;
    }
}
