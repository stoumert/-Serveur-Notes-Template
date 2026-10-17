lexer grammar FormulaLexer;

@header {
package fr.univ_amu.fr.formation.m1info.gla.technical;
}

WS: [ \t\r\n]+ -> skip;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
SEMICOLON: ';';

MAX: 'max';
MULTIPLY: '*';
ADD: '+';
VARIABLE: [A-Z][A-Z0-9]*;
NUMBER: DIGIT+ ((',' | '.') DIGIT+)?;

fragment DIGIT: [0-9];

ERROR_CHAR : . ;