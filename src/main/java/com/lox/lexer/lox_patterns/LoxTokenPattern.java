package com.lox.lexer.lox_patterns;

import java.util.regex.Matcher;

import com.lox.lexer.TokenType;
import com.lox.lexer.token_pattern.AbstractTokenPattern;

public abstract class LoxTokenPattern extends AbstractTokenPattern {

    @Override
    protected String getLexeme (Matcher matcher) {
        return matcher.group(1);
    }

    @Override
    protected Object getLiteral (String lexeme) {
        return lexeme;
    }

    /**
     * Implementations of LoxTokenPattern declared as inner classes to avoid having
     * to declare each in separate files.
     */

    /**
     * ===== SINGLE CHARACTER TOKENS =====
     */

    static public class LeftParen extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\()";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.LEFT_PAREN;
        }
    }

    static public class RightParen extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\))";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.RIGHT_PAREN;
        }
    }

    static public class LeftBrace extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\{)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.LEFT_BRACE;
        }
    }

    static public class RightBrace extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\})";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.RIGHT_BRACE;
        }
    }

    static public class Comma extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(,)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.COMMA;
        }
    }

    static public class Dot extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\.)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.DOT;
        }
    }

    static public class Minus extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\-)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.MINUS;
        }
    }

    static public class Plus extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\+)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.PLUS;
        }
    }

    static public class Semicolon extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(;)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.SEMICOLON;
        }
    }

    static public class Slash extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(/)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.SLASH;
        }
    }

    static public class Star extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\*)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.STAR;
        }
    }

    /**
     * ===== ONE/TWO CHARACTER TOKENS =====
     */

    static public class Bang extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(!)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.BANG;
        }
    }

    static public class BangEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(!=)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.BANG_EQUAL;
        }
    }

    static public class Equal extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(=)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.EQUAL;
        }
    }

    static public class EqualEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(==)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.EQUAL_EQUAL;
        }
    }

    static public class Greater extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(>)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.GREATER;
        }
    }

    static public class GreaterEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(>=)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.GREATER_EQUAL;
        }
    }

    static public class Less extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(<)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.LESS;
        }
    }

    static public class LessEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(<=)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.LESS_EQUAL;
        }
    }

    /**
     * ===== LITERALS =====
     */

    static public class Identifier extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^([a-zA-Z_][a-zA-Z0-9]*)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.IDENTIFIER;
        }
    }

    static public class StringLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^\"((?:\\.|[^\"\\])*)\"$";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.STRING;
        }
    }
    
    static public class NumberLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(-?\\d+(\\.\\d+)?)$";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.NUMBER;
        }

        @Override
        protected Object getLiteral (String string) {
            return Double.parseDouble(string);
        }
    }

    static public class TrueLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(true)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.TRUE;
        }

        @Override
        protected Object getLiteral (String string) {
            return true;
        }
    }

    static public class FalseLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(false)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.FALSE;
        }

        @Override
        protected Object getLiteral (String string) {
            return false;
        }
    }

    static public class NilLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(nil)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.NIL;
        }

        @Override
        protected Object getLiteral (String string) {
            return null;
        }
    }

    /**
     * ===== KEYWORDS =====
     */

    static public class And extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(and)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.AND;
        }
    }

    static public class Or extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(or)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.OR;
        }
    }

    static public class If extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(if)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.IF;
        }
    }

    static public class Else extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(else)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.ELSE;
        }
    }

    static public class Return extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(return)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.RETURN;
        }
    }

    static public class Var extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(var)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.VAR;
        }
    }

    static public class This extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(this)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.THIS;
        }
    }

    static public class For extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(for)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.FOR;
        }
    }

    static public class While extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(while)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.WHILE;
        }
    }

    static public class Print extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(print)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.PRINT;
        }
    }

    static public class Class extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(class)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.CLASS;
        }
    }

    static public class Fun extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(fun)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.FUN;
        }
    }

    static public class Super extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(super)";
        }

        @Override
        protected TokenType getTokenType() {
            return TokenType.SUPER;
        }
    }
}
