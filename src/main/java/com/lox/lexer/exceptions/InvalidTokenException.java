package com.lox.lexer.exceptions;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(String source) {
        super("Unrecognized token at: " + source);
    }
    
}
