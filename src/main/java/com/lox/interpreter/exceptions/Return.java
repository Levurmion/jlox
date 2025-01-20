package com.lox.interpreter.exceptions;

public class Return extends RuntimeException {
    final public Object value;

    public Return (Object value) {
        /**
         * Since this object is to be used to return values from functions in 'return' statements,
         * we don't need the JVM to create:
         * - error messages
         * - the stack trace
         */
        super(null, null, false, false);
        this.value = value;
    }
    
}
