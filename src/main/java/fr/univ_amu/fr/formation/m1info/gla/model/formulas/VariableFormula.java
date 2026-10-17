package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Map;
import java.util.Objects;

public class VariableFormula implements Formula {
    private final String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VariableFormula that = (VariableFormula) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public VariableFormula(String name) {
        this.name = name;
    }

    @Override
    public double getGrade(Map<String, Double> values) {
        Double grade = values.get(name);
        if (grade == null) {
            throw new IllegalArgumentException("No value provided for variable: " + name);
        }
        return grade;
    }

    @Override
    public double computeWeight() {
        return 1.0;
    }

    @Override
    public <R> R accept(FormulaVisitor<R> visitor) {
        return visitor.visitVariable(this);
    }

    public String getName() {
        return name;
    }
}
