package com.lox;

import java.util.Scanner;

import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.parser.exceptions.ParseError;

public class App
{
    final public Lox interpreter = new Lox();
    final public Scanner scanner = new Scanner(System.in);

    public static void main( String[] args )
    {   
        System.out.println("Starting Lox REPL v0.1");
        System.err.println("");
        App app = new App();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.scanner.close();
        }));

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
                System.err.println(e.getMessage());
            } catch (ParseError e) {
                System.err.println(e);
            } catch (Exception e) {
                app.scanner.close();
                throw e;
            } 
        }
    }
}
