package com.lox.interpreter;

import java.util.HashMap;
import java.util.Map;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.lexer.LoxToken;

public class Environment {
    static final public String UNINITIALIZED = "uninitialized";

    final private Map<String, Object> variables = new HashMap<>();

    public void define (LoxToken variable, Object value) {
        this.variables.put((String)variable.literal, value);
    }

    public void define (LoxToken variable) {
        this.variables.put((String)variable.literal, Environment.UNINITIALIZED);
    }

    public void assign (LoxToken variable, Object value) {
        if (this.variables.containsKey((String)variable.literal)) {
            this.variables.put((String)variable.literal, value);
        } else {
            throw new RuntimeError(variable, "cannot assign value to an undeclared variable");
        }
    }

    public Object use (LoxToken variable) {
        if (this.variables.containsKey((String)variable.literal)) {
            Object value = this.variables.get((String)variable.literal);
            if (value == Environment.UNINITIALIZED) {
                throw new RuntimeError(variable, "variable is uninitialized before use");
            } else {
                return value;
            }
        }
        throw new RuntimeError(variable, "cannot use an undeclared variable");
    }
}
