package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

public record VariableNode(String name) implements FormulaNode {
    @Override
    public <R> R accept(FormulaNodeVisitor<R> visitor) {
        return visitor.visitVariableNode(this);
    }
}
