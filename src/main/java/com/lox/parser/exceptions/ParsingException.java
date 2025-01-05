package com.lox.parser.exceptions;

import com.lox.lexer.Token;
import com.lox.lexer.lox.LoxTokenType;

public class ParsingException extends Exception {

    public ParsingException(Token<LoxTokenType> token) {
        super("Parse error at " + "(" + token.line + ":" + token.col + ")\n" + token);
    }

    public ParsingException(String customMessage) {
        super("Parse error: " + customMessage);
    }
    
}
