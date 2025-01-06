package com.lox.interpreter;

import com.lox.interfaces.AstNodeInterface;

public class LoxInterpreter {
    
    final private AstVisitor visitor = new AstVisitor();
    
    public Object interpret (AstNodeInterface ast) {
        return ast.accept(this.visitor);
    }
}
