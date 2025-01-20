package com.lox.parser.ast;

import java.util.List;

import com.lox.interpreter.Environment;
import com.lox.interpreter.LoxInterpreter;
import com.lox.interpreter.exceptions.Return;

public class LoxFunction implements LoxCallable {
    private final Stmt.FunDeclStmt declaration;

    public LoxFunction(Stmt.FunDeclStmt declaration) {
        this.declaration = declaration;
    }

    @Override 
    public int arity () {
        return this.declaration.parameters.size();
    } 

    @Override
    public Object call (LoxInterpreter interpreter, List<Object> arguments) {
        Environment funEnvironment = new Environment(interpreter.environment);

        for (int i = 0; i < declaration.parameters.size(); i++) {
            // define parameter values (arguments) in function scope environment
            funEnvironment.define(declaration.parameters.get(i), arguments.get(i));
        }

        try {
            interpreter.executeBlock(this.declaration.body, funEnvironment);
        } catch (Return ret) {
            return ret.value;
        }
        return null;
    }

    @Override
    public String toString () {
        return "<fn " + this.declaration.identifier.lexeme + ">";
    }
}
