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
                var identifier = 12;
                class MyClass {
                    method () {
                        print "Hello";
                    }
                }
                """;
        LoxLexer lexer = new LoxLexer(loxCode);
        lexer.tokenize();

        System.out.println(lexer.tokens);
    }
}
