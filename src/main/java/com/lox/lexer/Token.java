package com.lox.lexer;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    public int line;
    public int col;
    
    public Token (TokenType type, String lexeme, Object literal, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.col = col;
    }

    public Token (TokenType type, String lexeme, Object literal) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = 0;
        this.col = 0;
    }

    @Override
    public String toString () {
        return this.type + " " + this.lexeme + " " + this.literal + " (" + this.line + ":" + this.col + ")";
    }

    public void setLine (int line) {
        this.line = line;
    }

    public void setCol (int col) {
        this.col = col;
    }
}
