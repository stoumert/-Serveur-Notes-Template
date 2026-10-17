package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

public record CourseFormulaDTO(FormulaNode root, List<String> variableNames) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CourseFormulaDTO(FormulaNode root1, List<String> names))) return false;
        return Objects.equals(root, root1) && CollectionUtils.isEqualCollection(variableNames, names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, variableNames);
    }
}
