package com.lox;

import java.util.Scanner;

import com.lox.parser.ast.AstNode;
import com.lox.parser.exceptions.ParseError;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {   
        System.out.println("Starting Lox REPL v0.1");
        System.err.println("");
        Scanner scanner = new Scanner(System.in);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scanner.close();
        }));

        while (true) {
            try {
                System.out.print(">> ");
                String source = scanner.nextLine();
                Lox lox = new Lox();
                AstNode loxAst = lox.parse(source);
                System.out.println(loxAst);
            } catch (ParseError e) {
                System.err.println(e);
            }
        }
    }
}
