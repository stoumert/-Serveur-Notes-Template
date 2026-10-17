package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import fr.univ_amu.fr.formation.m1info.gla.data.Convertor;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaFactory;
import java.util.HashSet;


public class CourseFormulaConvertor implements Convertor<CourseFormula, CourseFormulaDTO> {
    private final FormulaFactory formulaFactory;

    public CourseFormulaConvertor(FormulaFactory formulaFactory) {
        this.formulaFactory = formulaFactory;
    }

    @Override
    public CourseFormula fromDTO(CourseFormulaDTO dto) {
        return new CourseFormula(dto.root().accept(new DTOToFormulaVisitor(formulaFactory)),
                new HashSet<>(dto.variableNames()));
    }

    @Override
    public CourseFormulaDTO toDTO(CourseFormula element) {
        return new CourseFormulaDTO(element.getFormula().accept(new FormulaToDTOVisitor()),
                element.getVariableNames().stream().toList());
    }
}
