package fr.univ_amu.fr.formation.m1info.gla.technical;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaFactory;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaParser;
import org.antlr.v4.runtime.*;

public class AntlrFormulaParser implements FormulaParser {
    private final AntlrFormulaVisitor formulaVisitor;

    public AntlrFormulaParser(FormulaFactory formulaFactory) {
        formulaVisitor = new AntlrFormulaVisitor(formulaFactory);
    }

    @Override
    public CourseFormula parse(String input) {
        CharStream stream = CharStreams.fromString(input);
        fr.univ_amu.fr.formation.m1info.gla.technical.FormulaLexer lexer =
                new fr.univ_amu.fr.formation.m1info.gla.technical.FormulaLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser parser =
                new fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(new FormulaParserErrorListener());
        var tree = parser.expression();
        return formulaVisitor.computeCourseFormula(tree);
    }
}
