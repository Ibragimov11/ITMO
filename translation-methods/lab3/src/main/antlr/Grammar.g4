grammar Grammar;

INPUT: 'input()';
PRINT: 'print';
TYPE: 'int';
IF: 'if';
ELIF: 'elif';
ELSE: 'else';
WHILE: 'while';
COLON: ':';
TAB: '\t' | '    ';
L_BRACKET: '(';
R_BRACKET: ')';
ASSIGNMENT: '=';
OPERATION: '+' | '-' | '*' | '/';
COMPARISON: '!=' | '>' | '>=' | '==' | '<=' | '<';
NUMBER: '0' | [1-9][0-9]*;
VARIABLE: [a-zA-Z][a-zA-Z0-9]*;

SPACE: ' ' -> skip;
WHITE_SPACE: [\r\n]+ -> skip;

program: statement+ EOF;
statement: assignment
    | ifelse
    | while
    | print;
assignment: VARIABLE ASSIGNMENT (input | expr);
input: TYPE L_BRACKET INPUT R_BRACKET;
ifelse: if elif* else?;
if: IF condition COLON inner;
elif: ELIF condition COLON inner;
else: ELSE COLON inner;
while: WHILE condition COLON inner;
condition: expr COMPARISON expr;
inner: inner_statement+;
inner_statement: TAB (assignment | print);
expr: expr OPERATION expr
    | L_BRACKET expr R_BRACKET
    | NUMBER
    | VARIABLE;
print: PRINT L_BRACKET expr R_BRACKET;
