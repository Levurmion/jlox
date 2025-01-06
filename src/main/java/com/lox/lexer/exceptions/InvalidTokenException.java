package com.lox.lexer.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String source) {
        super("Unrecognized token at: " + source);
    }
    
}
