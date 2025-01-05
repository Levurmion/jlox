package com.lox.parser;

import java.util.ArrayList;

import com.lox.lexer.lox.LoxToken;
import com.lox.lexer.lox.LoxTokenType;
import com.lox.parser.ast.AstNode;
import com.lox.parser.exceptions.ParsingException;

public class Parser {

    final private ArrayList<LoxToken> tokenStream;
    final private Grammar grammar;
    public AstNode ast;
    private int curr = 0;

    public Parser(ArrayList<LoxToken> tokenStream, Grammar grammar) {
        this.tokenStream = tokenStream;
        this.grammar = grammar;
    }

    public void parse() {
        Context ctx = new Context();
        try {
            this.ast = grammar.start(ctx);

            if (this.peek().type != LoxTokenType.EOF) {
                throw new ParsingException("incomplete parse");
            }
        } catch (ParsingException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean isAtEnd () {
        return this.curr >= this.tokenStream.size();
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
    }
}
