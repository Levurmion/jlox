package com.lox;

import com.lox.lexer.LoxToken;
import com.lox.lexer.LoxTokenType;

public class Lox {

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
