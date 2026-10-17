package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.util.Arrays;

import java.util.Map;

public class SumFormula extends VariadicOperator {

    public SumFormula(Formula... formulas) {
        super(formulas);
    }

    @Override
    public double getGrade(Map<String, Double> values) {
        double totalGrade = 0;
        for (Formula formula : formulas) {
            totalGrade += formula.getGrade(values) * formula.computeWeight();
        }
        return totalGrade;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SumFormula other)) return false;
        return Arrays.equalsUnordered(this.formulas, other.formulas);
    }

    @Override
    public <R> R accept(FormulaVisitor<R> visitor) {
        return visitor.visitSum(this);
    }
}
