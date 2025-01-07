package com.lox;

import java.util.Scanner;

import com.lox.interpreter.LoxInterpreter;
import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.parser.exceptions.ParseError;

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
        LoxInterpreter interpreter = new LoxInterpreter();

        while (true) {
            try {
                System.out.print(">> ");
                String source = scanner.nextLine();

                if (source.equals("exit")) {
                    scanner.close();
                    System.err.println("Exiting REPL\n");
                    break;
                }

                interpreter.interpret(source);
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
