package com.lox.parser;

import java.util.ArrayList;
import java.util.List;

import com.lox.Lox;
import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.ast.Stmt;
import com.lox.parser.exceptions.ParseError;

public class LoxParser {

    final private ArrayList<LoxToken> tokenStream;
    final private LoxGrammar grammar;
    public List<Stmt> program = new ArrayList<>();
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
                throw this.error(this.getLastMatchedToken(), message);
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
         * Matches all k-lookahead tokens.
         */
        public boolean kLookahead (LoxTokenType... tokenTypes) {
            for (int i = 0; i < tokenTypes.length; i++) {
                var tokenType = tokenTypes[i];
                var lookahead = tokenStream.get(curr + i);
                if (tokenType != lookahead.type) {
                    return false;
                }
            }
            return true;
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
        
        public boolean isAtEnd () {
            return isEOF();
        }
        
        public ParseError error (LoxToken token, String message) {
            Lox.error(token, message);
            return new ParseError();
        }
    }

    public void parse() {
        Context ctx = new Context();
        List<Stmt> program = grammar.start(ctx);
        if (this.peek().type != LoxTokenType.EOF) {
            Lox.error(this.peek(), "incomplete parse");
            throw new ParseError();
        } else {
            this.program = program;
        }
    }

    private boolean isEOF () {
        if (this.curr <= this.tokenStream.size() - 1) {
            return this.tokenStream.get(this.curr).type == LoxTokenType.EOF;
        }
        Lox.report("unterminated program: statements likely missing a trailing ';'");
        throw new ParseError();
    }

    private void advance () {
        this.curr++;
    }

    private boolean check (LoxTokenType tokenType) {
        var currToken = this.peek();
        return currToken.type == tokenType;
    }

    private LoxToken peek () {
        if (this.isEOF()) return this.tokenStream.getLast();
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

        while (!this.isEOF()) {
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
