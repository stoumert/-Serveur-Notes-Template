package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

public interface FormulaNodeVisitor<R> {
    R visitVariableNode(VariableNode variableNode);
    R visitSumNode(SumNode sumNode);
    R visitMaxNode(MaxNode maxNode);
    R visitWeightNode(WeightNode weightNode);
}
