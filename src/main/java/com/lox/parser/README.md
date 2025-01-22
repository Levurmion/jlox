# Lox Grammar

## Statements
The main purpose of statements is to execute a side effect that can be persisted in the interpreter.

```
<program>               -> <declaration>* "EOF"
<declaration>           -> <var_declaration> | <statement>

// variable declaration
<var_declaration>       -> "var" IDENTIFIER [ "=" <expression_statement> ]? ";"

// named function declaration
<fun_declaration>       -> "fun" <function> <block>
<function>              -> IDENTIFIER "(" <parameters>? ")"
<parameters>            -> IDENTIFIER [ "," IDENTIFIER ]*

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
<return_statement>      -> "return" <expression>? ";"
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
The evaluation of an expression always yields a value.

```
<expression>            -> <assignment> | <anonymous_fun>
<assignment>            -> [ IDENTIFIER "=" ]? <assignment> | <logic_or>

// anonymous function expression - yields a reference to a 'LoxFunction' object
<anonymous_fun>         -> "fun" "(" <parameters>? ")" <block>

<logic_or>              -> <logic_and> [ "or" <logic_and> ]*
<logic_and>             -> <equality> [ "and" <equality> ]*

<equality>              -> <comparison> [[ "!=" | "==" ] <comparison> ]*
<comparison>            -> <term> [[ "<" | ">" | "<=" | ">=" ] <term> ]*

<term>                  -> <factor> [[ "-" | "+" ] <factor>]*
<factor>                -> <unary> [[ "*" | "/" ] <unary>]*
<unary>                 -> [ "-" | "!" ] <unary> | <call>

<call>                  -> <primary> [ "(" <argument>? ")" ]*
<argument>              -> <expression> [ "," <expression> ]*

<primary>               -> NUMBER 
                        | STRING 
                        | IDENTIFIER 
                        | "true" 
                        | "false" 
                        | "nil" 
                        | "(" <expression> ")"
```