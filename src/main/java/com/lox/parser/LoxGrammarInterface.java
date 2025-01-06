package com.lox.parser;

import com.lox.interfaces.AstNodeInterface;

public interface LoxGrammarInterface {
    public AstNodeInterface start (LoxParser.Context ctx);
}
