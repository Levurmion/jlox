# Lox Grammar

## Statements
The main purpose of statements is to execute a side effect that can be persisted in the interpreter.

```
<program>               -> <declaration>* "EOF"
<declaration>           -> <var_declaration> | <statement>

// declarations
<var_declaration>       -> "var" ID [ "=" <expression_statement> ]? ";"

// statements
<statement>             -> <print_statement> 
                        | <break_statement>
                        | <continue_statement>
                        | <expression_statement> 
                        | <if_statement> 
                        | <while_statement> 
                        | <for_statement>
                        | <block>

// keyword statements
<print_statement>       -> "print" <expression> ";"
<break_statement>       -> "break" ";"
<continue_statement>    -> "continue" ";"

// control flow statements
<if_statement>          -> "if" "(" <expression> ")" <statement> <else_if_statement>*
<else_if_statement>     -> "else" "if" "(" <expression> ")" <statement> <else_if_statement> | <else_statement>?
<else_statement>        -> "else" <statement>

// loops
<while_statement>       -> "while" "(" <expression> ")" <statement>
<for_statement>         -> "for" "(" <for_initializer> <expression>? ";" <expression>? ")" <statement>
<for_initializer>       -> <var_declaration> | <expression_statement> | ";"

// blocks
<block>                 -> "{" <declaration>* "}"

// expression statement
<expression_statement>  -> <expression> ";"
```

## Expressions
The evaluation of expression always yields a value.

```
<expression>            -> <assignment>
<assignment>            -> ID "=" <assignment> | <logic_or>
<logic_or>              -> <logic_and> [ "or" <logic_and> ]*
<logic_and>             -> <equality> [ "and" <equality> ]*
<equality>              -> <comparison> [[ "!=" | "==" ] <comparison> ]*
<comparison>            -> <term> [[ "<" | ">" | "<=" | ">=" ] <term> ]*
<term>                  -> <factor> [[ "-" | "+" ] <factor>]*
<factor>                -> <unary> [[ "*" | "/" ] <unary>]*
<unary>                 -> [ "-" | "!" ] <unary> | <primary>
<primary>               -> NUMBER | STRING | ID | "true" | "false" | "nil" | "(" <expression> ")"
```