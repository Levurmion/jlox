package com.lox.parser;

import java.util.ArrayList;

import com.lox.Lox;
import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.ast.Expr;
import com.lox.parser.exceptions.ParseError;

public class LoxParser {

    final private ArrayList<LoxToken> tokenStream;
    final private LoxGrammar grammar;
    public Expr ast = null;
    private int curr = 0;

    public LoxParser(ArrayList<LoxToken> tokenStream, LoxGrammar grammar) {
        this.tokenStream = tokenStream;
        this.grammar = grammar;
    }

    /**
     * The `Parser.Context` object provides methods injected into `Grammar` rules to
     * interact with tokens and control the `Parser's` progress.
     */
    public class Context {

        /**
         * Matches a token in the current position against a collection of valid
         * `tokenTypes`. If matched, returns `true` and advances the parser by one
         * token.
         */
        public boolean match (LoxTokenType... tokenTypes) {
            for (var tokenType : tokenTypes) {
                if (check(tokenType)) {
                    advance();
                    return true;
                }
            }
            return false;
        }

        /**
         * Match a single token in the current position and throw a `ParseError`
         * if the token does not match. 
         */
        public boolean matchOrThrow (LoxTokenType tokenType, String message) {
            boolean tokenMatched = this.match(tokenType);
            if (!tokenMatched) {
                throw this.error(this.getCurrToken(), message);
            } else {
                return true;
            }
        }

        /**
         * Matches a token in the current position against a collection of valid
         * `tokenTypes` without advancing the parser.
         */
        public boolean lookahead (LoxTokenType... tokenTypes) {
            for (var tokenType : tokenTypes) {
                if (check(tokenType)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Get the token that was last matched by the parser.
         */
        public LoxToken getLastMatchedToken () {
            if (curr == 0) {
                return null;
            } else {
                return tokenStream.get(curr - 1);
            }
        }

        /**
         * Returns a token at the current position.
         */
        public LoxToken getCurrToken () {
            return peek();
        }
        
        public ParseError error (LoxToken token, String message) {
            Lox.error(token, message);
            return new ParseError();
        }
    }

    public void parse() {
        Context ctx = new Context();
        Expr ast = grammar.start(ctx);
        if (this.peek().type != LoxTokenType.EOF) {
            Lox.error(this.peek(), "incomplete parse");
            throw new ParseError();
        } else {
            this.ast = ast;
        }
    }

    private boolean isAtEnd () {
        return this.tokenStream.get(this.curr).type == LoxTokenType.EOF;
    }

    private void advance () {
        this.curr++;
    }

    private boolean check (LoxTokenType tokenType) {
        var currToken = this.peek();
        return currToken.type == tokenType;
    }

    private LoxToken peek () {
        if (this.isAtEnd()) return this.tokenStream.getLast();
        else {
            return this.tokenStream.get(this.curr);
        }
    }

    private LoxToken peekPrevious () {
        if (this.curr == 0) return null;
        else {
            return this.tokenStream.get(this.curr - 1);
        }
    }

    private void synchronize () {
        // skip the current erroneous token
        this.advance();

        while (!this.isAtEnd()) {
            // discard tokens until the next statement boundary (demarcated by SEMICOLON)
            if (this.peekPrevious().type == LoxTokenType.SEMICOLON) return;

            switch (this.peek().type) {
                case CLASS:
                case FUN:
                case FOR:
                case WHILE:
                case IF:
                case ELSE:
                case VAR:
                case PRINT:
            }

            this.advance();
        }
    }

}
