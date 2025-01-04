package com.lox.lexer.token_pattern;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lox.lexer.Token;

public abstract class AbstractTokenPattern<TokenTypes> {
    final private Pattern pattern;
    private Matcher matcher;

    public AbstractTokenPattern () {
        String regex = this.getRegex();
        this.pattern = Pattern.compile(regex);
    }
    
    public Optional<Token<TokenTypes>> match(String source) {
        this.matcher = this.pattern.matcher(source);
        
        if (this.matcher.find()) {
            String lexeme = this.getLexeme(this.matcher);
            Object literal = this.getLiteral(lexeme);
            Token<TokenTypes> token = new Token<TokenTypes>(this.getTokenType(), lexeme, literal);
            return Optional.of(token);
        }
        
        return Optional.empty();
    }

    abstract protected String getRegex();
    abstract protected TokenTypes getTokenType();
    abstract protected String getLexeme (Matcher matcher);
    abstract protected Object getLiteral (String lexeme);
} 
