parser grammar FormulaParser;

@header {
package fr.univ_amu.fr.formation.m1info.gla.technical;
}

options { tokenVocab=FormulaLexer; }


expression
    : NUMBER MULTIPLY expression # weightedRightExpression
    | expression MULTIPLY NUMBER # weightedLeftExpression
    | VARIABLE # variable
    | MAX OPEN_PAREN expression (SEMICOLON expression)+ CLOSE_PAREN # max
    | expression (ADD expression)+ # addition
    ;