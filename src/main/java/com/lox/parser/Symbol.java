package com.lox.parser;

import java.util.ArrayList;
import java.util.Optional;

import com.lox.parser.ast.AstNode;
import com.lox.parser.exceptions.ParsingException;

public class Symbol {

    public ArrayList<Symbol> children = new ArrayList<>();

    // Each Symbol can optionally emit a corresponding AstNode
    public Optional<AstNode> emitAstNode () {
        return Optional.empty();
    }
    
    static class Terminal extends Symbol {
        final public Object literal;

        public Terminal(Object literal) {
            this.literal = literal;
        }
    }

    static abstract class NonTerminal extends Symbol {
        abstract public void production (Parser.Context ctx, Symbol parent) throws ParsingException;
    }
}
