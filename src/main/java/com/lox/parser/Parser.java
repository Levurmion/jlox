package com.lox.parser;

import java.util.ArrayList;
import java.util.Optional;

import com.lox.lexer.Token;
import com.lox.lexer.lox.LoxTokenType;

public class Parser {

    final private ArrayList<Token<LoxTokenType>> tokenStream;
    private int curr = 0;

    public Parser(ArrayList<Token<LoxTokenType>> tokenStream) {
        this.tokenStream = tokenStream;
    }

    private boolean isAtEnd () {
        return this.curr >= this.tokenStream.size();
    }

    private void advance () {
        this.curr++;
    }

    private boolean check (LoxTokenType tokenType) {
        var currToken = this.peek();
        if (currToken.isPresent()) {
            return currToken.get().type == tokenType;
        } else {
            return false;
        }
    }

    private Optional<Token<LoxTokenType>> peek () {
        if (this.isAtEnd()) return Optional.empty();
        else {
            return Optional.of(this.tokenStream.get(this.curr));
        }
    }

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
        public Optional<Token<LoxTokenType>> getLastMatchedToken () {
            if (curr == 0) {
                return Optional.empty();
            } else {
                return Optional.of(tokenStream.get(curr - 1));
            }
        }

        /**
         * Returns a token at the current position.
         */
        public Optional<Token<LoxTokenType>> getCurrToken () {
            return peek();
        }
    }
}
