package com.lox;

import java.util.Scanner;

import com.lox.interpreter.exceptions.RuntimeError;
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
        Lox lox = new Lox();

        while (true) {
            try {
                System.out.print(">> ");
                String source = scanner.nextLine();

                if (source.equals("exit")) {
                    scanner.close();
                    System.err.println("Exiting REPL\n");
                    break;
                }

                lox.parse(source);
                Object result = lox.eval();
                System.out.println(result);
            } catch (RuntimeError e) {
                System.err.println(e.token);
                System.err.println(e.getMessage());
            } catch (ParseError e) {
                System.err.println(e);
            } catch (Exception e) {
                scanner.close();
                throw e;
            } 
        }
    }
}
