package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Arrays;

public abstract class VariadicOperator implements Formula{
    protected final Formula[] formulas;

    public VariadicOperator(Formula... formulas) {
        this.formulas = Arrays.copyOf(formulas, formulas.length);
    }

    public Formula[] getFormulas() {
        return formulas;
    }

    @Override
    public double computeWeight() {
        double totalWeight = 0;
        for (Formula formula : formulas) {
            totalWeight += formula.computeWeight();
        }
        return totalWeight;
    }

}
