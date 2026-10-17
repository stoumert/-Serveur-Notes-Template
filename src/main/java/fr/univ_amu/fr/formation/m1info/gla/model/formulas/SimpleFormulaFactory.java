package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Set;

public class SimpleFormulaFactory implements FormulaFactory{
    @Override
    public Formula createSum(Formula... formulas) {
        requiresAtLeastTwoFormulas(formulas);
        return new SumFormula(formulas);
    }

    private void requiresAtLeastTwoFormulas(Formula[] formulas) {
        if (formulas.length < 2) {
            throw new IllegalArgumentException("At least one formula is required");
        }
    }

    @Override
    public Formula createWeightedFormula(Formula formula, double weight) {
        return new WeightedFormula(formula, weight);
    }

    @Override
    public Formula createMax(Formula... formulas) {
        requiresAtLeastTwoFormulas(formulas);
        return new MaxFormula(formulas);
    }

    @Override
    public Formula createVariable(String name) {
        return new VariableFormula(name);
    }

    @Override
    public CourseFormula createCourseFormula(Formula formula, Set<String> variableNames) {
        return new CourseFormula(formula, variableNames);
    }
}
