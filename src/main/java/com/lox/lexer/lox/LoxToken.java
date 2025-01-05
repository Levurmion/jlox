package com.lox.lexer.lox;

import com.lox.lexer.Token;

public class LoxToken extends Token<LoxTokenType> {
    public LoxToken(LoxTokenType type, String lexeme, Object literal) {
        super(type, lexeme, literal);
    }
}