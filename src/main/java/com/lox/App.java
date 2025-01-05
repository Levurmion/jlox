package com.lox;

import com.lox.lexer.lox.LoxLexer;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {   
        String loxCode = """
                1 + 2 * (5 / 2);
                """;
        LoxLexer lexer = new LoxLexer(loxCode);
        lexer.tokenize();

        System.out.println(lexer.tokens);
    }
}
