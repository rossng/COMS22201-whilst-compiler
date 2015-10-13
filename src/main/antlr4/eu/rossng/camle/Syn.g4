grammar Syn;
import Lex;

program :
    statements
  ;

statements :
    statement ( SEMICOLON statement )*
  ;

statement :
    WRITE OPENPAREN ( INTNUM | STRING ) CLOSEPAREN
  | WRITELN
  ;
