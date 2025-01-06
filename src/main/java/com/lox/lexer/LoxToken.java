package com.lox.lexer;

public class LoxToken {
    public final LoxTokenType type;
    public final String lexeme;
    public final Object literal;
    public int line;
    public int col;
    
    public LoxToken (LoxTokenType type, String lexeme, Object literal, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.col = col;
    }

    public LoxToken (LoxTokenType type, String lexeme, Object literal) {
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