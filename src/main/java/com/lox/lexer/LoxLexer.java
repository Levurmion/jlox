package com.lox.lexer;

import com.lox.lexer.lox_patterns.LoxTokenPattern;

public class LoxLexer {
    private String source;
    private int line = 1;
    private int col = 0;

    private LoxTokenPattern[] patterns = {
        // match single-character tokens first
        new LoxTokenPattern.LeftParen(),
        new LoxTokenPattern.RightParen(),
        new LoxTokenPattern.LeftBrace(),
        new LoxTokenPattern.RightBrace(),
        new LoxTokenPattern.Comma(),
        new LoxTokenPattern.Dot(),
        new LoxTokenPattern.Minus(),
        new LoxTokenPattern.Plus(),
        new LoxTokenPattern.Semicolon(),
        new LoxTokenPattern.Slash(),
        new LoxTokenPattern.Star(),

        // match one/two character tokens next in the correct order

        // two character tokens first
        new LoxTokenPattern.BangEqual(),
        new LoxTokenPattern.EqualEqual(),
        new LoxTokenPattern.GreaterEqual(),
        new LoxTokenPattern.LessEqual(),

        // then one-character tokens
        new LoxTokenPattern.Bang(),
        new LoxTokenPattern.Equal(),
        new LoxTokenPattern.Greater(),
        new LoxTokenPattern.Less(),

        // match primitives
        new LoxTokenPattern.Identifier(),
        new LoxTokenPattern.StringLiteral(),
        new LoxTokenPattern.NumberLiteral(),
        new LoxTokenPattern.TrueLiteral(),
        new LoxTokenPattern.FalseLiteral(),
        new LoxTokenPattern.NilLiteral(),

        // match keywords
        new LoxTokenPattern.And(),
        new LoxTokenPattern.Or(),
        new LoxTokenPattern.If(),
        new LoxTokenPattern.Else(),
        new LoxTokenPattern.Fun(),
        new LoxTokenPattern.Class(),
        new LoxTokenPattern.This(),
        new LoxTokenPattern.Super(),
        new LoxTokenPattern.Print(),
        new LoxTokenPattern.Var(),
        new LoxTokenPattern.For(),
        new LoxTokenPattern.While()
    };
    
    public LoxLexer (String source) {
        this.source = source;
    }
}
