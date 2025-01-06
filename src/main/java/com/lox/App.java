package com.lox;

import java.util.Scanner;

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
        Lox lox = new Lox();

        while (true) {
            try {
                System.out.print(">> ");
                String source = scanner.nextLine();
                lox.parse(source);
                Object result = lox.eval();
                System.out.println(result);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
