grammar Whilst;

program     : statements
            ;

statements  : statement SEMICOLON statements                                    # StatementsSequence
            | statement                                                         # StatementsSingle
            ;

statement   : ID ASSIGN exp                                                     # StatementAssign
            | SKIP                                                              # StatementSkip
            | IF boolexp
              THEN statement
              ELSE statement                                                    # StatementIfThenElse
            | WHILE boolexp
              DO statement                                                      # StatementWhile
            | READ OPENPAREN ID CLOSEPAREN                                      # StatementRead
            | WRITE OPENPAREN ( exp ) CLOSEPAREN                                # StatementWriteExp
            | WRITE OPENPAREN ( boolexp ) CLOSEPAREN                            # StatementWriteBoolExp
            | WRITE OPENPAREN ( STRING ) CLOSEPAREN                             # StatementWriteString
            | WRITE OPENPAREN ( ID ) CLOSEPAREN                                 # StatementWriteId
            | WRITELN                                                           # StatementWriteLn
            | OPENPAREN statements CLOSEPAREN                                   # StatementStatements
            ;

boolexp     : boolterm ( AND boolterm )*
            ;

boolterm    : NOT bool
            | bool
            ;

bool        : TRUE
            | FALSE
            | exp EQUAL exp
            | exp LEQUAL exp
            | OPENPAREN boolexp CLOSEPAREN
            ;

exp         : exp TIMES exp                                                     # ExpTimes
            | exp op=( PLUS | MINUS ) exp                                       # ExpPlusMinus
            | ID                                                                # ExpId
            | INTNUM                                                            # ExpIntNum
            | OPENPAREN exp CLOSEPAREN                                          # ExpBracketed
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

// OPERATORS
AND        : '&' ;
NOT        : '!' ;
EQUAL      : '=' ;
LEQUAL     : '<=' ;
PLUS       : '+' ;
MINUS      : '-' ;
TIMES      : '*' ;
ASSIGN     : ':=' ;

// LITERALS
FALSE      : 'false' ;
TRUE       : 'true' ;

// OTHER
ID         : [a-zA-Z] [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? [a-zA-Z0-9]? ;

INTNUM     : [0-9]+ ;

STRING     : '\'' ('\'' '\'' | ~'\'')* '\'';

WS         : [ \t\r\n\u000C]+ -> skip ;

COMMENT    : '{' (~'}')* '}' -> skip ;