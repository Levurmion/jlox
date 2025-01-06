package com.lox;

import com.lox.lexer.LoxLexer;
import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;
import com.lox.parser.ast.AstNode;

public class Lox {

    private LoxLexer lexer;
    private LoxParser parser;

    public Lox() {}

    public AstNode parse (String source) {
        this.lexer = new LoxLexer(source);
        lexer.tokenize();

        this.parser = new LoxParser(this.lexer.tokens, new LoxGrammar());
        this.parser.parse();

        return this.parser.ast;
    }

    public static void report (Object... args) {
        String message = "";
        for (var msg : args) {
            message = message + msg.toString();
        }
        System.out.println(message);
    }

    public static void error (LoxToken token, String message) {
        if (token.type == LoxTokenType.EOF) {
            report(token.line, " at end\n", message);
        } else {
            report(token.line, " at '", token.lexeme, "' (", token.type, ")\n", message);
        }
    }
}
