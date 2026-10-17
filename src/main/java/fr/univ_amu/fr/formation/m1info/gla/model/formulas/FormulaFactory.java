package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import java.util.Set;

public interface FormulaFactory {
    Formula createSum(Formula... formulas);
    Formula createVariable(String name);
    Formula createMax(Formula... formulas);
    CourseFormula createCourseFormula(Formula formula, Set<String> variableNames);
    Formula createWeightedFormula(Formula formula, double weight);
}
