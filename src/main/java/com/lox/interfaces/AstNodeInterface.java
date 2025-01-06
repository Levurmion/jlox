package com.lox.interfaces;

import com.lox.interpreter.AstVisitor;

public interface AstNodeInterface {
    public Object accept (AstVisitor visitor);
}
