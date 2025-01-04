package com.lox.lexer.token_pattern;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lox.lexer.Token;
import com.lox.lexer.TokenType;

public abstract class AbstractTokenPattern {
    final private Pattern pattern;
    private Matcher matcher;

    public AbstractTokenPattern () {
        String regex = this.getRegex();
        this.pattern = Pattern.compile(regex);
    }
    
    public Optional<Token> match(String source) {
        this.matcher = this.pattern.matcher(source);
        
        if (this.matcher.find()) {
            String lexeme = this.getLexeme(this.matcher);
            Object literal = this.getLiteral(lexeme);
            Token token = new Token(this.getTokenType(), lexeme, literal);
            return Optional.of(token);
        }
        
        return Optional.empty();
    }

    abstract protected String getRegex();
    abstract protected TokenType getTokenType();
    abstract protected String getLexeme (Matcher matcher);
    abstract protected Object getLiteral (String lexeme);
} 
