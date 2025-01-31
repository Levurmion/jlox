package com.lox.lexer;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LoxTokenPattern {

    final private Pattern pattern;
    private Matcher matcher;

    public LoxTokenPattern () {
        String regex = this.getRegex();
        this.pattern = Pattern.compile(regex);
    }
    
    public Optional<LoxToken> match(String source) {
        this.matcher = this.pattern.matcher(source);
        
        if (this.matcher.find()) {
            String lexeme = this.getLexeme(this.matcher);
            Object literal = this.getLiteral(lexeme);
            LoxToken token = new LoxToken(this.getTokenType(), lexeme, literal);
            return Optional.of(token);
        }
        
        return Optional.empty();
    }
    
    protected String getLexeme (Matcher matcher) {
        return matcher.group(1);
    }
    
    protected Object getLiteral (String lexeme) {
        return lexeme;
    }

    abstract protected String getRegex();
    abstract protected LoxTokenType getTokenType();

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
            return "^([a-zA-Z_][a-zA-Z0-9_]*)";
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
            return "^(\\d+(\\.\\d+)?)";
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
            return "^(\\btrue\\b)";
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
            return "^(\\bfalse\\b)";
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
            return "^(\\bnil\\b)";
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

    static public class Break extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bbreak\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.BREAK;
        }
    }

    static public class Continue extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bcontinue\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.CONTINUE;
        }
    }

    static public class And extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\band\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.AND;
        }
    }

    static public class Or extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bor\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.OR;
        }
    }

    static public class If extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bif\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.IF;
        }
    }

    static public class Else extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\belse\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.ELSE;
        }
    }

    static public class Return extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\breturn\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.RETURN;
        }
    }

    static public class Var extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bvar\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.VAR;
        }
    }

    static public class This extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bthis\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.THIS;
        }
    }

    static public class For extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bfor\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.FOR;
        }
    }

    static public class While extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bwhile\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.WHILE;
        }
    }

    static public class Print extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bprint\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.PRINT;
        }
    }

    static public class Class extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bclass\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.CLASS;
        }
    }

    static public class Fun extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bfun\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.FUN;
        }
    }

    static public class Super extends LoxTokenPattern {
        @Override
        protected String getRegex () {
            return "^(\\bsuper\\b)";
        }

        @Override
        protected LoxTokenType getTokenType() {
            return LoxTokenType.SUPER;
        }
    }
}
