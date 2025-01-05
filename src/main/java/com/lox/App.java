package com.lox;

import com.lox.lexer.lox.LoxLexer;
import com.lox.parser.Grammar;
import com.lox.parser.Parser;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {   
        String loxCode = """
                1 == !!2 <= 4.5;
                """;
        LoxLexer lexer = new LoxLexer(loxCode);
        lexer.tokenize();

        Parser parser = new Parser(lexer.tokens, new Grammar());
        parser.parse();

        System.err.println(parser.ast);
    }
}
