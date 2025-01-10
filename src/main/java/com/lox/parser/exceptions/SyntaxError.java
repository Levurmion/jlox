package com.lox.parser.exceptions;

import com.lox.lexer.LoxToken;

public class SyntaxError extends RuntimeException {
    final public LoxToken token;

    public SyntaxError(LoxToken token, String message) {
        super("SyntaxError: " + message);
        this.token = token;
    }
}
