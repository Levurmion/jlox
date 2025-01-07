package com.lox.interpreter.exceptions;

import com.lox.lexer.LoxToken;

public class RuntimeError extends RuntimeException {
    final public LoxToken token;

    public RuntimeError(LoxToken token, String message) {
        super(message);
        this.token = token;
    }
}
