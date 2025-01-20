package com.lox;

import java.util.Scanner;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.parser.exceptions.ParseError;
import com.lox.parser.exceptions.SyntaxError;

public class App
{
    final public Lox interpreter = new Lox();
    final public Scanner scanner = new Scanner(System.in);

    public static void debugRuntimeError (RuntimeException e, boolean debugMode) {
        if (debugMode) {
            throw e;
        } else {
            System.err.println(e);
        }
    }

    public static void main( String[] args )
    {   
        System.out.println("Starting Lox REPL v0.1");
        System.err.println("");
        App app = new App();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.scanner.close();
        }));

        CommandLine cmd = new CommandLine(args);
        boolean debugMode = cmd.hasLongFlag("debug");

        while (true) {
            try {
                if (app.interpreter.sourceIsEmpty()) {
                    System.out.print(">>> ");
                } else {
                    System.out.print("... ");
                }

                String source = app.scanner.nextLine();

                switch (source) {
                    case "\\exit": {
                        app.scanner.close();
                        System.out.println("Exiting REPL\n");
                        return;
                    } case "\\run": {
                        app.interpreter.interpretSource();
                        System.out.println();
                        break;
                    } default: {
                        app.interpreter.writeLine(source);
                    }
                }

            } catch (RuntimeError e) {
                System.err.println(e.token);
                debugRuntimeError(e, debugMode);
            } catch (SyntaxError e) {
                System.err.println(e.token);
                debugRuntimeError(e, debugMode);
            } catch (ParseError e) {
                debugRuntimeError(e, debugMode);
            } catch (Exception e) {
                app.scanner.close();
                throw e;
            } 
        }
    }
}
