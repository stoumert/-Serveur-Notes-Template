package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

public interface FormulaVisitor<R> {
    R visitSum(SumFormula sumFormula);
    R visitVariable(VariableFormula variableFormula);
    R visitMax(MaxFormula maxFormula);
    R visitWeightedFormula(WeightedFormula weightedFormula);
}
