package com.lox.interpreter;

import com.lox.lexer.LoxLexer;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;

public class LoxInterpreter {
    private LoxLexer lexer;
    private LoxParser parser;
    final private AstVisitor visitor = new AstVisitor();
    
    public Object interpret (String source) {
        this.lexer = new LoxLexer(source);
        this.lexer.tokenize();

        this.parser = new LoxParser(this.lexer.tokens, new LoxGrammar());
        this.parser.parse();

        return this.parser.ast.accept(visitor);
    }
}
