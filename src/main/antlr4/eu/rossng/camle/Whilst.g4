grammar Whilst;

program :
    statements
  ;

statements :
    statement ( SEMICOLON statement )*
  ;

statement :
    ID ASSIGN exp
  | SKIP
  | IF boolexp
    THEN statement
    ELSE statement
  | WHILE boolexp
    DO statement
  | READ OPENPAREN ID CLOSEPAREN
  | WRITE OPENPAREN ( exp | boolexp | STRING | ID ) CLOSEPAREN
  | WRITELN
  | OPENPAREN statements CLOSEPAREN
  ;

boolexp : boolterm ( AND boolterm )* ;

boolterm :
    NOT bool
  | bool
  ;

bool :
    TRUE
  | FALSE
  | exp EQUAL exp
  | exp LEQUAL exp
  | OPENPAREN boolexp CLOSEPAREN
  ;

exp : term ( ( PLUS | MINUS ) term )* ;

term : factor ( TIMES factor )* ;

factor :
      ID
    | INTNUM
    | OPENPAREN exp CLOSEPAREN
    ;

// KEYWORDS
DO         : 'do' ;
ELSE       : 'else' ;
IF         : 'if' ;
READ       : 'read' ;
SKIP       : 'skip' ;
THEN       : 'then' ;
WHILE      : 'while' ;
WRITE      : 'write' ;
WRITELN    : 'writeln' ;

// GRAMMAR
SEMICOLON  : ';' ;
OPENPAREN  : '(' ;
CLOSEPAREN : ')' ;

// IDENTIFIERS
ID         : [a-zA-Z] [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? ;

// LITERALS
INTNUM     : [0-9]+ ;

STRING     : '\'' ('\'' '\'' | ~'\'')* '\'';

COMMENT    : '{' (~'}')* '}' -> skip ;

WS         : (' ' | '\t' | '\r' | '\n' )+ -> skip ;

FALSE      : 'false' ;
TRUE       : 'true' ;

// OPERATORS

AND        : '&' ;
NOT        : '!' ;
EQUAL      : '=' ;
LEQUAL     : '<=' ;
PLUS       : '+' ;
MINUS      : '-' ;
TIMES      : '*' ;
ASSIGN     : ':=' ;