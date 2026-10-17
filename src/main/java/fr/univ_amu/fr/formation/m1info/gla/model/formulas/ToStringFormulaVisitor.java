package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

public class ToStringFormulaVisitor implements FormulaVisitor<String> {
    public String getString(CourseFormula formula) {
        return formula.getFormula().accept(this);
    }

    @Override
    public String visitSum(SumFormula sumFormula) {
        System.out.println("Visiting Sum");
        StringBuilder result = new StringBuilder();
        Formula[] children = sumFormula.getFormulas();
        for(int i = 0; i < children.length-1; i++) {
            result.append(children[i].accept(this)).append(" + ");
        }
        result.append(children[children.length-1].accept(this));
        return result.toString();
    }

    @Override
    public String visitVariable(VariableFormula variableFormula) {
        return variableFormula.getName();
    }

    @Override
    public String visitMax(MaxFormula maxFormula) {
        StringBuilder result = new StringBuilder("max(");
        Formula[] children = maxFormula.getFormulas();
        for(int i = 0; i < children.length-1; i++) {
            result.append(children[i].accept(this)).append("; ");
        }
        result.append(children[children.length-1].accept(this));
        result.append(")");
        return result.toString();
    }

    @Override
    public String visitWeightedFormula(WeightedFormula weightedFormula) {
        return weightedFormula.getWeight() + "*" + weightedFormula.getFormula().accept(this);
    }
}
