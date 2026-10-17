package fr.univ_amu.fr.formation.m1info.gla.storage.xml;
import fr.univ_amu.fr.formation.m1info.gla.data.formulas.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class CourseFormulaDTOXMLConvertorTest {
    String variableXML;
    private String complexXML;

    @BeforeEach
    void setUp() {
        variableXML = """
                <CourseFormulaDTO>
                    <root type="variable">
                        <name>X</name>
                    </root>
                    <variableNames>
                        <variableNames>X</variableNames>
                    </variableNames>
                </CourseFormulaDTO>""";
        try {
            complexXML = IOUtils.toString(
                    this.getClass().getResourceAsStream("/formula_dto.xml"),
                    StandardCharsets.UTF_8
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void testToXML_variableFormula(){
        FormulaCourseDTOXMLConvertor convertor = new FormulaCourseDTOXMLConvertor();
        CourseFormulaDTO formula =
                new CourseFormulaDTO(new VariableNode("X"), List.of("X"));
        String actualXML = convertor.toXML(formula);
        assertThat(XMLUtil.removeSpacesOutsideTags(actualXML))
                .isEqualTo(XMLUtil.removeSpacesOutsideTags(variableXML));
    }


    @Test
    void testToXML_complexFormula(){
        FormulaCourseDTOXMLConvertor convertor = new FormulaCourseDTOXMLConvertor();
        CourseFormulaDTO formula = getCourseFormulaDTO();
        String actualXML = convertor.toXML(formula);
        assertThat(XMLUtil.removeSpacesOutsideTags(actualXML))
                .isEqualTo(XMLUtil.removeSpacesOutsideTags(complexXML));
    }

    @NotNull
    private static CourseFormulaDTO getCourseFormulaDTO() {
        VariableNode varET = new VariableNode("ET");
        VariableNode varCC = new VariableNode("CC");
        SumNode sum = new SumNode(List.of(
                new WeightNode(0.5, varET),
                new WeightNode(0.5, varCC)));
        return new CourseFormulaDTO(new MaxNode(List.of(varET, sum)), List.of("ET", "CC"));
    }


    @Test
    void testFromXML_variableFormula(){
        CourseFormulaDTO dto = new FormulaCourseDTOXMLConvertor().fromXML(variableXML);
        assertThat(dto).isEqualTo(
                new CourseFormulaDTO(new VariableNode("X"), List.of("X"))
        );
    }

    @Test
    void testFromXML_complexFormula(){
        CourseFormulaDTO dto = new FormulaCourseDTOXMLConvertor().fromXML(complexXML);
        assertThat(dto).isEqualTo(getCourseFormulaDTO());
    }
}
