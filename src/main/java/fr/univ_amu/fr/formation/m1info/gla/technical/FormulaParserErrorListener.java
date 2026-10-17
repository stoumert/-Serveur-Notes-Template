package fr.univ_amu.fr.formation.m1info.gla.technical;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class FormulaParserErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new FormulaParserException("Error while parsing formula at position " + charPositionInLine + ": " + msg);
    }
}
