package com.lox.parser.ast;

import java.util.List;

import com.lox.interpreter.LoxInterpreter;

public interface LoxCallable {
    Object call (LoxInterpreter interpreter, List<Object> arguments);
    int arity ();
}
