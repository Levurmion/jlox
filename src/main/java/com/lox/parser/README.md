# Lox Grammar

## Statements
The main purpose of statements is to execute a side effect that can be persisted in the interpreter.

```
<program>               -> <declaration>* "EOF"
<declaration>           -> <var declaration> | <statement>

// declarations
<var declaration>       -> "var" ID [ "=" <expression statement> ]? ";"

// statements
<statement>             -> <print statement> | <expression statement> | <if statement> | <block>

// keyword statements
<print statement>       -> "print" <expression> ";"

// control flow statements
<if statement>          -> "if" "(" <expression> ")" <statement> <else if statement>*
<else if statement>     -> "else" "if" "(" <expression> ")" <statement> <else if statement> | <else statement>?
<else statement>        -> "else" <statement>

// blocks
<block>                 -> "{" [ <declaration> ]* "}"

// expression statement
<expression statement>  -> <expression> ";"
```

## Expressions
The evaluation of expression always yields a value.

```
<expression>            -> <assignment>
<assignment>            -> ID "=" <assignment> | <equality>
<equality>              -> <comparison> [[ "!=" | "==" ] <comparison> ]*
<comparison>            -> <term> [[ "<" | ">" | "<=" | ">=" ] <term> ]*
<term>                  -> <factor> [[ "-" | "+" ] <factor>]*
<factor>                -> <unary> [[ "*" | "/" ] <unary>]*
<unary>                 -> [ "-" | "!" ] <unary> | <primary>
<primary>               -> NUMBER | STRING | ID | "true" | "false" | "nil" | "(" <expression> ")"
```