package com.lox.parser;

import com.lox.parser.ast.AstNode;

public interface LoxGrammarInterface {
    public AstNode start (LoxParser.Context ctx);
}
