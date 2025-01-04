package com.lox.lexer.lox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import com.lox.lexer.Token;
import com.lox.lexer.exceptions.InvalidTokenException;

public class LoxLexer {
    private String source;
    private int line = 1;
    private int col = 0;
    private int current = 0;
    
    final private Pattern newlinePattern = Pattern.compile("^(\\n|\\r)");
    final private Pattern whitespacePattern = Pattern.compile("^\\s");

    public ArrayList<Token<LoxTokenType>> tokens = new ArrayList<>();

    final private LoxTokenPattern[] patterns = {
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

        // finally, match keywords
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

    private Token<LoxTokenType> scanToken () throws Exception {
        for (LoxTokenPattern pattern : this.patterns) {
            var result = pattern.match(this.source);
            if (result.isPresent()) {
                return result.get();
            }
        }

        throw new InvalidTokenException(this.source);
    }

    private void handleWhitespace () {
        boolean isNewline = this.newlinePattern.matcher(this.source).find();
        boolean isWhitespace = this.whitespacePattern.matcher(this.source).find();

        if (isNewline) {
            this.line++;
            this.col = 0;
            this.current++;
        } else if (isWhitespace) {
            this.col++;
            this.current++;
        }
    }

    public void tokenize () {

    }

}
