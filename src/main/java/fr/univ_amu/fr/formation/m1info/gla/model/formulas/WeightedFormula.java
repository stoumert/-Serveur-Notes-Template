package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Objects;

public class WeightedFormula implements Formula {
    private final Formula formula;
    private final double weight;

    public WeightedFormula(Formula formula, double weight) {
        this.formula = formula;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WeightedFormula that = (WeightedFormula) o;
        return Double.compare(weight, that.weight) == 0 && Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, weight);
    }

    @Override
    public double getGrade(java.util.Map<String, Double> values) {
        return formula.getGrade(values);
    }

    @Override
    public double computeWeight() {
        return weight * formula.computeWeight();
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public <R> R accept(FormulaVisitor<R> visitor) {
        return visitor.visitWeightedFormula(this);
    }

    public Formula getFormula() {
        return formula;
    }
}
