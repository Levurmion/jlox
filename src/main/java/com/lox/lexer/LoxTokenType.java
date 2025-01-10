package com.lox.lexer;

public enum LoxTokenType {
    // single char tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA,
    DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // one/two char tokens
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // primitive literals
    IDENTIFIER, STRING, NUMBER, TRUE, FALSE, NIL,

    // keywords
    AND, CLASS, ELSE, FUN, FOR, IF, OR, PRINT, RETURN,
    SUPER, THIS, VAR, WHILE, BREAK, CONTINUE,

    EOF
}
