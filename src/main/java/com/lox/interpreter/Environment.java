package com.lox.interpreter;

import java.util.HashMap;
import java.util.Map;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.lexer.LoxToken;

public class Environment {
    static final public String UNINITIALIZED = "uninitialized";

    private Environment enclosing;
    final private Map<String, Object> variables = new HashMap<>();

    public Environment (Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Environment () {
        this.enclosing = null;
    }

    public void define (String name, Object value) {
        this.variables.put(name, value);
    }

    public void define (String name) {
        this.variables.put(name, Environment.UNINITIALIZED);
    }

    public void define (LoxToken variable, Object value) {
        this.variables.put((String)variable.literal, value);
    }

    public void define (LoxToken variable) {
        this.variables.put((String)variable.literal, Environment.UNINITIALIZED);
    }

    public void assign (LoxToken variable, Object value) {
        if (this.variables.containsKey((String)variable.literal)) {
            this.variables.put((String)variable.literal, value);
        } else if (this.enclosing != null) {
            this.enclosing.assign(variable, value);
        } else {
            throw new RuntimeError(variable, "undeclared variable");
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
        } else if (this.enclosing != null) {
            return this.enclosing.use(variable);
        } else {
            throw new RuntimeError(variable, "cannot use an undeclared variable: " + variable.lexeme);
        }
    }
}
