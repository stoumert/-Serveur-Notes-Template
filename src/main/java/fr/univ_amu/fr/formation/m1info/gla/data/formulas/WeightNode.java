package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

public record WeightNode(double weight, FormulaNode child) implements FormulaNode {
    @Override
    public <R> R accept(FormulaNodeVisitor<R> visitor) {
        return visitor.visitWeightNode(this);
    }
}
