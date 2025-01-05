package com.lox.parser;

import com.lox.parser.ast.AstNode;
import com.lox.parser.exceptions.ParsingException;

public abstract class Symbol {
    abstract public AstNode production (Parser.Context ctx) throws ParsingException;
}
