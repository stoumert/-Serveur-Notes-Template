package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CourseFormula {
    private final Formula formula;
    private final Set<String> variableNames;

    public CourseFormula(Formula formula, Set<String> variableNames) {
        this.variableNames = variableNames;
        this.formula = formula;
    }

    public Set<String> getVariableNames() {
        return variableNames;
    }

    public double getGrade(java.util.Map<String, Double> values) {
        requiresAllGradesOfTheCourse(values);
        return formula.getGrade(values);
    }

    private void requiresAllGradesOfTheCourse(Map<String, Double> values) {
        if(values == null || values.isEmpty()) {
            throw new IllegalArgumentException("No grades provided for the course.");
        }
        Set<String> providedVariableNames = values.keySet();
        if(!variableNames.equals(providedVariableNames)) {
            throw new IllegalArgumentException("Grades provided do not match the course variables.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CourseFormula that = (CourseFormula) o;
        return Objects.equals(formula, that.formula) && Objects.equals(variableNames, that.variableNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, variableNames);
    }

    public Formula getFormula() {
        return formula;
    }
}
