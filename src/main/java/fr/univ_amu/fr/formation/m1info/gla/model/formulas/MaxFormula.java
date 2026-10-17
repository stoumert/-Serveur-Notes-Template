package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.util.Arrays;

import java.util.Map;

public class MaxFormula extends VariadicOperator {

    public MaxFormula(Formula... formulas) {
        super(formulas);
    }

    @Override
    public double getGrade(Map<String, Double> values) {
        double maxGrade = Double.NEGATIVE_INFINITY;
        for (Formula formula : formulas) {
            double grade = formula.getGrade(values);
            if (grade > maxGrade) {
                maxGrade = grade;
            }
        }
        return maxGrade;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MaxFormula other)) return false;
        return Arrays.equalsUnordered(this.formulas, other.formulas);
    }

    @Override
    public <R> R accept(FormulaVisitor<R> visitor) {
        return visitor.visitMax(this);
    }
}
