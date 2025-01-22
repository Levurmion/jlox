package com.lox.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.lox.interpreter.exceptions.Return;
import com.lox.interpreter.exceptions.RuntimeError;
import com.lox.interpreter.helpers.ExprHelper;
import com.lox.lexer.LoxLexer;
import com.lox.lexer.LoxToken;
import com.lox.parser.LoxGrammar;
import com.lox.parser.LoxParser;
import com.lox.parser.ast.Expr;
import com.lox.parser.ast.LoxCallable;
import com.lox.parser.ast.LoxFunction;
import com.lox.parser.ast.Stmt;
import com.lox.parser.exceptions.SyntaxError;

public class LoxInterpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private LoxLexer lexer;
    private LoxParser parser;

    // interpreter states
    final private Environment globals = new Environment();
    private Environment environment;

    public LoxInterpreter () {
        // define a 'clock' native function
        this.globals.define("clock", new LoxCallable() {
            @Override
            public int arity() { return 0; }

            @Override
            public Object call(LoxInterpreter interpreter, List<Object> arguments) {
                return (double)System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() { return "<native fn>"; }

        });

        this.environment = new Environment(globals);
    }
    
    public void interpret (String source) {
        this.lexer = new LoxLexer(source);
        this.lexer.tokenize();
        this.parser = new LoxParser(this.lexer.tokens, new LoxGrammar());
        this.parser.parse();

        for (var statement : this.parser.program) {
            this.execute(statement);
        }
    }

    private Void execute (Stmt statement) {
        statement.accept(this);
        return null;
    }

    public Void executeBlock (Stmt.BlockStmt block, Environment blockEnvironment) {
        Environment outerScope = this.environment;
        this.environment = blockEnvironment;

        try {
            for (var declaration : block.declarations) {
                this.execute(declaration);
            }
        } finally {
            this.environment = outerScope;
        }

        return null;
    }

    private Object evaluate (Expr expression) {
        if (expression == null) {
            return null;
        }
        return expression.accept(this);
    }

    // ===== STATEMENT VISITOR METHODS =====

    @Override
    public Void visitVarDeclStmt (Stmt.VarDeclStmt varDeclStmt) {
        Expr expression = varDeclStmt.expression;
        if (expression == null) {
            this.environment.define(varDeclStmt.identifier);
        } else {
            this.environment.define(varDeclStmt.identifier, this.evaluate(expression));
        }
        return null;
    }

    @Override 
    public Void visitFunDeclStmt (Stmt.FunDeclStmt funDeclStmt) {
        LoxFunction loxFunction = new LoxFunction(funDeclStmt, environment);
        this.environment.define(funDeclStmt.identifier.lexeme, loxFunction);
        return null;
    }

    @Override
    public Void visitBlockStmt (Stmt.BlockStmt blockStmt) {
        // create a inner scope
        Environment blockScope = new Environment(this.environment);
        Environment outerScope = this.environment;
        this.environment = blockScope;

        try {
            // execute block declarations
            for (var declaration : blockStmt.declarations) {
                this.execute(declaration);
            }
        } finally {
            // delete inner scope
            this.environment = outerScope;
        }

        return null;
    }
    
    @Override
    public Void visitExpressionStmt (Stmt.ExpressionStmt exprStmt) {
        this.evaluate(exprStmt.expression);
        return null;
    }
    
    @Override
    public Void visitPrintStmt (Stmt.PrintStmt printStmt) {
        Object expressionValue = this.evaluate(printStmt.expression);
        System.out.println(expressionValue);
        return null;
    }

    @Override
    public Void visitReturnStmt (Stmt.ReturnStmt returnStmt) {
        Object value = null;
        if (returnStmt.expression != null) {
            value = evaluate(returnStmt.expression);
        }
        throw new Return(value);
    }

    @Override
    public Void visitIfStmt (Stmt.IfStmt ifStmt) {
        if (ExprHelper.isTruthy(this.evaluate(ifStmt.condition))) {
            return this.execute(ifStmt.statement);
        }

        for (var elseIf : ifStmt.elseIfStatements) {
            if (ExprHelper.isTruthy(this.evaluate(elseIf.condition))) {
                return this.execute(elseIf.statement);
            }
        }
        
        if (ifStmt.elseStatement != null) {
            return this.execute(ifStmt.elseStatement);
        }

        return null;
    }

    @Override
    public Void visitWhileStmt (Stmt.WhileStmt whileStmt) {
        while (ExprHelper.isTruthy(this.evaluate(whileStmt.condition))) {
            try {
                this.execute(whileStmt.statement);
            } catch (SyntaxError err) {
                switch (err.token.type) {
                    case BREAK: 
                        return null;
                    case CONTINUE: 
                        continue;
                    default:
                        throw err;
                }
            }
        }
        return null;
    }

    @Override 
    public Void visitSingleKeywordStmt (Stmt.SingleKeywordStmt singleKeywordStmt) {
        LoxToken keyword = singleKeywordStmt.keyword;
        switch (keyword.type) {
            case BREAK:
                throw new SyntaxError(keyword, "cannot use 'break' outside a loop");
            case CONTINUE:
                throw new SyntaxError(keyword, "cannot use 'continue' outside a loop");
            default:
                return null;
        }
    }
    
    // ===== EXPRESSION VISITOR METHODS =====

    @Override
    public Object visitAssignmentExpr (Expr.Assignment assignment) {
        Expr right = assignment.right;
        Object rightValue = this.evaluate(right);
        this.environment.assign(assignment.variable, rightValue);

        return rightValue;
    }

    @Override
    public Object visitAnonymousFuncExpr (Expr.AnonymousFunc anonymousFunc) {
        Stmt.FunDeclStmt anonymousFuncDecl = new Stmt.FunDeclStmt(
            anonymousFunc.parameters,
            anonymousFunc.body
        );
        return new LoxFunction(anonymousFuncDecl, environment);
    }
    
    @Override
    public Object visitGroupingExpr (Expr.Grouping group) {
        return this.evaluate(group.expression);
    }
    
    @Override
    public Object visitBinaryExpr (Expr.Binary binary) {
        Object left = this.evaluate(binary.left);
        Object right = this.evaluate(binary.right);
        LoxToken operator = binary.operator;

        Object result = null;
        switch (operator.type) {
            case PLUS: {
                if (ExprHelper.isString(left, right)) {
                    result = (String)left + (String)right;
                } else if (ExprHelper.isNumber(left, right)) {
                    result = (Double)left + (Double)right;
                } else {
                    throw new RuntimeError(operator, "operands must be of type: 'String' or 'Number'");
                }
                break;
            }
            case MINUS: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left - (Double)right;
                break;
            }
            case STAR: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left * (Double)right;
                break;
            }
            case SLASH: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left / (Double)right;
                break;
            }
            case LESS: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left < (Double)right;
                break;
            }
            case GREATER: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left > (Double)right;
                break;
            }
            case LESS_EQUAL: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left <= (Double)right;
                break;
            }
            case GREATER_EQUAL: {
                ExprHelper.assertNumberOperands(operator, left, right);
                result = (Double)left >= (Double)right;
                break;
            }
            case EQUAL_EQUAL: {
                result = ExprHelper.isEqual(left, right);
                break;
            }
            case BANG_EQUAL: {
                result = !ExprHelper.isEqual(left, right);
                break;
            }
            case AND: {
                result = ExprHelper.isTruthy(left) && ExprHelper.isTruthy(right);
                break;
            }
            case OR: {
                result = ExprHelper.isTruthy(left) || ExprHelper.isTruthy(right);
                break;
            }
            default: {
                throw new RuntimeError(operator, "invalid binary operator");
            }
        }

        return result;
    }

    @Override
    public Object visitUnaryExpr (Expr.Unary unary) {
        LoxToken operator = unary.operator;
        Object right = this.evaluate(unary.right);

        Object result = null;
        switch (operator.type) {
            case MINUS:
                ExprHelper.assertNumberOperand(operator, right);
                result = -(Double)right;                
                break;
            case BANG:
                result = !(ExprHelper.isTruthy(right));
                break;
            default:
                throw new RuntimeError(operator, "invalid unary operator");
        }

        return result;
    }

    @Override
    public Object visitCallExpr (Expr.Call expr) {
        Object callee = this.evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(this.evaluate(argument));
        }

        if (!(callee instanceof LoxCallable)) {
            throw new RuntimeError(expr.paren, "can only call functions and classes");
        }

        LoxCallable callable = (LoxCallable)callee;
        if (arguments.size() != callable.arity()) {
            throw new RuntimeError(expr.paren, "Expected " + callable.arity() + " arguments but got " + arguments.size());
        }

        return callable.call(this, arguments);
    }

    @Override
    public Object visitLiteralExpr (Expr.Literal literal) {
        return literal.token.literal;
    }

    @Override
    public Object visitVariableExpr (Expr.Variable variable) {
        return this.environment.use(variable.token);
    }
}
