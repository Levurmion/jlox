package com.lox.lexer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import com.lox.lexer.exceptions.InvalidTokenException;

public class LoxLexer {
    final private String source;
    private int line = 1;
    private int col = 0;
    private int current = 0;
    
    final private Pattern newlinePattern = Pattern.compile("^(\\n|\\r)");
    final private Pattern whitespacePattern = Pattern.compile("^\\s");

    public ArrayList<LoxToken> tokens = new ArrayList<>();

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
        new LoxTokenPattern.While(),
    
        // finally, match primitives and generics
        new LoxTokenPattern.StringLiteral(),
        new LoxTokenPattern.NumberLiteral(),
        new LoxTokenPattern.TrueLiteral(),
        new LoxTokenPattern.FalseLiteral(),
        new LoxTokenPattern.NilLiteral(),
        new LoxTokenPattern.Identifier(),
    };
    
    public LoxLexer (String source) {
        this.source = source;
    }

    private LoxToken scanToken (String currSource) throws InvalidTokenException {
        for (LoxTokenPattern pattern : this.patterns) {
            Optional<LoxToken> result = pattern.match(currSource);
            if (result.isPresent()) {
                return result.get();
            }
        }

        throw new InvalidTokenException(currSource);
    }

    private boolean handleWhitespace (String currSource) {
        boolean isNewline = this.newlinePattern.matcher(currSource).find();
        boolean isWhitespace = this.whitespacePattern.matcher(currSource).find();

        if (isNewline) {
            this.line++;
            this.col = 0;
            this.current++;
            return true;
        } else if (isWhitespace) {
            this.col++;
            this.current++;
            return true;
        }

        return false;
    }

    public void tokenize () {
        while (this.current < this.source.length()) {
            String currSource = this.source.substring(this.current);
            if (this.handleWhitespace(currSource)) {
                continue;
            } 
            
            try {
                LoxToken token = this.scanToken(currSource);
                this.col += token.lexeme.length();
                this.current += token.lexeme.length();
                token.setLine(line);
                token.setCol(col);
                this.tokens.add(token);
            } catch (InvalidTokenException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        // add EOF token to end of input
        tokens.add(new LoxToken(
            LoxTokenType.EOF, 
            "$", 
            "$"
        ));
    }

}
