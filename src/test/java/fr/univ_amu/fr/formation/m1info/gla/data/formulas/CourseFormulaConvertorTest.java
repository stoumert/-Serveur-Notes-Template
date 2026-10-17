package fr.univ_amu.fr.formation.m1info.gla.data.formulas;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CourseFormulaConvertorTest {
    private CourseFormulaDTO variableCourseFormulaDTO;
    private CourseFormulaDTO complexCourseFormulaDTO;
    private CourseFormula variableCourseFormula;
    private CourseFormula complexCourseFormula;

    @BeforeEach
    void setUp() {
        variableCourseFormulaDTO = new CourseFormulaDTO(
                new VariableNode("A"),
                List.of("A")
        );
        complexCourseFormulaDTO = new CourseFormulaDTO(
                new MaxNode(List.of(
                        new SumNode(List.of(new WeightNode(0.5, new VariableNode("ET")),
                                        new WeightNode(0.5, new VariableNode("CC")))),
                        new VariableNode("ET"))), List.of("ET", "CC"));
        variableCourseFormula = new CourseFormula(
                new VariableFormula("A"),
                Set.of("A")
        );
        complexCourseFormula = new CourseFormula(
                new MaxFormula(new SumFormula(
                        new WeightedFormula(new VariableFormula("ET"), 0.5),
                        new WeightedFormula(new VariableFormula("CC"), 0.5)),
                        new VariableFormula("ET")), Set.of("ET", "CC"));
    }

    @Test
    void testToDTO_variableCourseFormula() {
        CourseFormulaConvertor convertor = new CourseFormulaConvertor(new SimpleFormulaFactory());
        CourseFormulaDTO dto = convertor.toDTO(variableCourseFormula);
        assertThat(dto).isEqualTo(variableCourseFormulaDTO);
    }

    @Test
    void testToDTO_complexCourseFormula() {
        CourseFormulaConvertor convertor = new CourseFormulaConvertor(new SimpleFormulaFactory());
        CourseFormulaDTO dto = convertor.toDTO(complexCourseFormula);
        assertThat(dto).isEqualTo(complexCourseFormulaDTO);
    }

    @Test
    void testFromDTO_variableCourseFormula() {
        CourseFormulaConvertor convertor = new CourseFormulaConvertor(new SimpleFormulaFactory());
        CourseFormula formula = convertor.fromDTO(variableCourseFormulaDTO);
        assertThat(formula).isEqualTo(variableCourseFormula);
    }

    @Test
    void testFromDTO_complexCourseFormula() {
        CourseFormulaConvertor convertor = new CourseFormulaConvertor(new SimpleFormulaFactory());
        CourseFormula formula = convertor.fromDTO(complexCourseFormulaDTO);
        assertThat(formula).isEqualTo(complexCourseFormula);
    }



}
