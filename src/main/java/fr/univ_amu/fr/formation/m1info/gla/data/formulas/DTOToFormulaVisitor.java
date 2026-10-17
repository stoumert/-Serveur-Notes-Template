package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.Formula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaFactory;

public class DTOToFormulaVisitor implements FormulaNodeVisitor<Formula> {
    private final FormulaFactory formulaFactory;

    public DTOToFormulaVisitor(FormulaFactory formulaFactory) {
        this.formulaFactory = formulaFactory;
    }

    @Override
    public Formula visitVariableNode(VariableNode node) {
        return formulaFactory.createVariable(node.name());
    }

    @Override
    public Formula visitSumNode(SumNode sumNode) {
        return formulaFactory.createSum(sumNode.children().stream()
                .map(n-> n.accept(this)).toArray(Formula[]::new));
    }

    @Override
    public Formula visitMaxNode(MaxNode maxNode) {
        return formulaFactory.createMax(maxNode.children().stream()
                .map(n-> n.accept(this)).toArray(Formula[]::new));
    }

    @Override
    public Formula visitWeightNode(WeightNode weightNode) {
        return formulaFactory.createWeightedFormula(
                weightNode.child().accept(this),
                weightNode.weight()
        );
    }
}
