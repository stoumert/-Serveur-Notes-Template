package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import java.util.List;

public record MaxNode(List<FormulaNode> children) implements FormulaNode{
    @Override
    public <R> R accept(FormulaNodeVisitor<R> visitor) {
        return visitor.visitMaxNode(this);
    }
}
