package com.lox.parser;

import java.text.ParseException;

import com.lox.lexer.lox.LoxTokenPattern;
import com.lox.lexer.lox.LoxTokenType;
import com.lox.parser.exceptions.ParsingException;

public class Grammar {

    static class Primary extends Symbol.NonTerminal {
        @Override
        public void production (Parser.Context ctx, Symbol parent) throws ParsingException {
            Symbol.Terminal primarySymbol = null;

            if (ctx.match(LoxTokenType.NUMBER)) {
                primarySymbol = new Symbol.Terminal((double)ctx.getLastMatchedToken().get().literal);
            } else if (ctx.match(LoxTokenType.STRING)) {
                primarySymbol = new Symbol.Terminal((String)ctx.getLastMatchedToken().get().literal);
            } else if (ctx.match(LoxTokenType.TRUE)) {
                primarySymbol = new Symbol.Terminal(true);
            } else if (ctx.match(LoxTokenType.FALSE)) {
                primarySymbol = new Symbol.Terminal(true);
            } else if (ctx.match(LoxTokenType.NIL)) {
                primarySymbol = new Symbol.Terminal(null);
            } else if (ctx.match(LoxTokenType.LEFT_PAREN)) {
                primarySymbol = new Symbol.Terminal("(");
            } else {
                throw new ParsingException(ctx.getCurrToken().get());
            }

            parent.children.add(primarySymbol);

            if (primarySymbol.literal == "(") {
                Symbol.NonTerminal nestedExpression = new Expression();
                parent.children.add(nestedExpression);

                // process the nested expression
                nestedExpression.production(ctx, nestedExpression);

                // when it returns, the next token must be a RIGHT_PAREN
                if (ctx.match(LoxTokenType.RIGHT_PAREN)) {
                    parent.children.add(new Symbol.Terminal(")"));
                } else {
                    throw new ParsingException(ctx.getCurrToken().get());
                }
            }
        }
    }

    static class Unary extends Symbol.NonTerminal {

        private boolean matchUnary (Parser.Context ctx) {
            return ctx.match(LoxTokenType.BANG, LoxTokenType.MINUS);
        }

        @Override
        public void production (Parser.Context ctx, Symbol parent) throws ParsingException {
            while (this.matchUnary(ctx)) {
                var matchedToken = ctx.getLastMatchedToken();
                parent.children.add(new Symbol.Terminal(matchedToken.get().literal));
            }
            
            // finally, expand the Primary
            if (
                ctx.lookahead(
                    LoxTokenType.NUMBER,
                    LoxTokenType.STRING,
                    LoxTokenType.TRUE, 
                    LoxTokenType.FALSE,
                    LoxTokenType.NIL,
                    LoxTokenType.LEFT_PAREN
                )
            ) {
                Primary primary = new Primary();
                parent.children.add(primary);
                primary.production(ctx, primary);
            } else {
                throw new ParsingException(ctx.getCurrToken().get());
            }
        }
    }

    static class Factor extends Symbol.NonTerminal {

        private boolean expectsUnary (Parser.Context ctx) {
            return (
                ctx.lookahead(
                    LoxTokenType.NUMBER,
                    LoxTokenType.STRING,
                    LoxTokenType.TRUE,
                    LoxTokenType.FALSE,
                    LoxTokenType.NIL,
                    LoxTokenType.LEFT_PAREN,
                    LoxTokenType.BANG,
                    LoxTokenType.MINUS
                )
            );
        }

        @Override
        public void production (Parser.Context ctx, Symbol parent) throws ParsingException {
            if (this.expectsUnary(ctx)) {
                Unary firstUnary = new Unary();
                parent.children.add(firstUnary);
                firstUnary.production(ctx, firstUnary);

                // after processing each unary, check if we are expecting another unary
                while (true) { 
                    Symbol.Terminal factorSymbol = null;
                    if (ctx.match(LoxTokenType.STAR)) {
                        factorSymbol = new Symbol.Terminal("*");
                    } else if (ctx.match(LoxTokenType.SLASH)) {
                        factorSymbol = new Symbol.Terminal("/");
                    }

                    if (factorSymbol != null) {
                        parent.children.add(factorSymbol);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    static class Expression extends Symbol.NonTerminal {
        @Override
        public void production (Parser.Context ctx) throws ParsingException {}
    }
}
