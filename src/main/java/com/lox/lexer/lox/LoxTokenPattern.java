package com.lox.lexer.lox;

import java.util.regex.Matcher;

import com.lox.lexer.token_pattern.AbstractTokenPattern;

public abstract class LoxTokenPattern extends AbstractTokenPattern<LoxTokenType> {

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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.LEFT_PAREN;
        }
    }

    static public class RightParen extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\))";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.RIGHT_PAREN;
        }
    }

    static public class LeftBrace extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\{)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.LEFT_BRACE;
        }
    }

    static public class RightBrace extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\})";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.RIGHT_BRACE;
        }
    }

    static public class Comma extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(,)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.COMMA;
        }
    }

    static public class Dot extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\.)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.DOT;
        }
    }

    static public class Minus extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\-)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.MINUS;
        }
    }

    static public class Plus extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\+)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.PLUS;
        }
    }

    static public class Semicolon extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(;)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.SEMICOLON;
        }
    }

    static public class Slash extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(/)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.SLASH;
        }
    }

    static public class Star extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\*)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.STAR;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.BANG;
        }
    }

    static public class BangEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(!=)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.BANG_EQUAL;
        }
    }

    static public class Equal extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(=)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.EQUAL;
        }
    }

    static public class EqualEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(==)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.EQUAL_EQUAL;
        }
    }

    static public class Greater extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(>)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.GREATER;
        }
    }

    static public class GreaterEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(>=)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.GREATER_EQUAL;
        }
    }

    static public class Less extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(<)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.LESS;
        }
    }

    static public class LessEqual extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(<=)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.LESS_EQUAL;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.IDENTIFIER;
        }
    }

    static public class StringLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^\"((?:\\\\.|[^\"\\\\])*)\"";
        }

        @Override
        protected String getLexeme (Matcher matcher) {
            return matcher.group(0);
        }

        @Override
        protected String getLiteral (String lexeme) {
            return lexeme.substring(1, lexeme.length()-1);
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.STRING;
        }
    }
    
    static public class NumberLiteral extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(-?\\d+(\\.\\d+)?)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.NUMBER;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.TRUE;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.FALSE;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.NIL;
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
        protected LoxTokenType getTokenType() {
            return LoxTokenType.AND;
        }
    }

    static public class Or extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(or)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.OR;
        }
    }

    static public class If extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(if)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.IF;
        }
    }

    static public class Else extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(else)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.ELSE;
        }
    }

    static public class Return extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(return)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.RETURN;
        }
    }

    static public class Var extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(var)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.VAR;
        }
    }

    static public class This extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(this)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.THIS;
        }
    }

    static public class For extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(for)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.FOR;
        }
    }

    static public class While extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(while)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.WHILE;
        }
    }

    static public class Print extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(print)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.PRINT;
        }
    }

    static public class Class extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(class)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.CLASS;
        }
    }

    static public class Fun extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(fun)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.FUN;
        }
    }

    static public class Super extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(super)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.SUPER;
        }
    }
}
