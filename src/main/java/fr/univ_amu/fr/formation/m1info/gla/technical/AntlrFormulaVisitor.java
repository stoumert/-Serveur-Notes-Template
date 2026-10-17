package fr.univ_amu.fr.formation.m1info.gla.technical;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.Formula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntlrFormulaVisitor extends fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParserBaseVisitor<Formula>{
    private final FormulaFactory formulaFactory;
    private final Set<String> variableNames = new HashSet<>();


    public AntlrFormulaVisitor(FormulaFactory formulaFactory) {
        this.formulaFactory = formulaFactory;
    }

    @Override
    public Formula visitWeightedRightExpression(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.WeightedRightExpressionContext ctx) {
        double weight = Double.parseDouble(ctx.NUMBER().getText().replace(',', '.'));
        return formulaFactory.createWeightedFormula(ctx.expression().accept(this), weight);
    }

    public Formula visitWeightedLeftExpression(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.WeightedLeftExpressionContext ctx) {
        double weight = Double.parseDouble(ctx.NUMBER().getText().replace(',', '.'));
        return formulaFactory.createWeightedFormula(ctx.expression().accept(this), weight);
    }

    public CourseFormula computeCourseFormula(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.ExpressionContext expressionContext) {
        Formula formula = visit(expressionContext);
        return formulaFactory.createCourseFormula(formula, variableNames);
    }

    @Override
    public Formula visitMax(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.MaxContext ctx) {
        Formula[] formulas = childrenFormulas(ctx.expression());
        return formulaFactory.createMax(formulas);
    }

    private Formula[] childrenFormulas(List<fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.ExpressionContext> children) {
        Formula[] formulas = new Formula[children.size()];
        for (int i = 0; i < children.size(); i++) {
            formulas[i] = visit(children.get(i));
        }
        return formulas;
    }

    @Override
    public Formula visitVariable(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.VariableContext ctx) {
        String variableName = ctx.VARIABLE().getText();
        variableNames.add(variableName);
        return formulaFactory.createVariable(variableName);
    }


    @Override
    public Formula visitAddition(fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParser.AdditionContext ctx) {
        Formula[] formulas = childrenFormulas(ctx.expression());
        return formulaFactory.createSum(formulas);
    }
}
