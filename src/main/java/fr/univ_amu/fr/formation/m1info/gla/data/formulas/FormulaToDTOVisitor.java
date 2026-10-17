package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.*;

import java.util.Arrays;

public class FormulaToDTOVisitor implements FormulaVisitor<FormulaNode> {
    @Override
    public FormulaNode visitSum(SumFormula sumFormula) {
        return new SumNode(Arrays.stream(sumFormula.getFormulas()).map(f -> f.accept(this)).toList());
    }

    @Override
    public FormulaNode visitVariable(VariableFormula variableFormula) {
        return new VariableNode(variableFormula.getName());
    }

    @Override
    public FormulaNode visitMax(MaxFormula maxFormula) {
        return new MaxNode(Arrays.stream(maxFormula.getFormulas()).map(f -> f.accept(this)).toList());
    }

    @Override
    public FormulaNode visitWeightedFormula(WeightedFormula weightedFormula) {
        return new WeightNode(weightedFormula.getWeight(), weightedFormula.getFormula().accept(this));
    }
}
