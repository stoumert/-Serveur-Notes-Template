package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Map;

public interface Formula {
    double getGrade(Map<String, Double> values);
    double computeWeight();
    <R> R accept(FormulaVisitor<R> visitor);
}
