package fr.univ_amu.fr.formation.m1info.gla.technical;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.SimpleFormulaFactory;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.ToStringFormulaVisitor;
import fr.univ_amu.fr.formation.m1info.gla.technical.FormulaParserException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class AntlrFormulaParserTest {
    @Test
    public void testSimpleParse() {
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse("0,5 * A + 0,5 * B");
        assertThat(courseFormula.getVariableNames()).isEqualTo(Set.of("A", "B"));
        var values1 = Map.of("A", 12.0, "B", 14.0);
        assertThat(courseFormula.getGrade(values1)).isCloseTo(13.0, withinPercentage(0.0001));
        var values2 = Map.of("A", 6.0, "B", 2.0);
        assertThat(courseFormula.getGrade(values2)).isCloseTo(4.0, withinPercentage(0.0001));
    }

    @Test
    public void testComplexParse() {
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse("max(ET; 0.5*ET + 0.5*CC)");
        assertThat(courseFormula.getVariableNames()).isEqualTo(Set.of("ET", "CC"));
        var values1 = Map.of("ET", 14.0, "CC", 15.0);
        assertThat(courseFormula.getGrade(values1)).isCloseTo(14.5, withinPercentage(0.0001));
        var values2 = Map.of("ET", 10.0, "CC", 8.0);
        assertThat(courseFormula.getGrade(values2)).isCloseTo(10, withinPercentage(0.0001));
    }

    @Test
    public void testThrownExceptions_WithEmptyValuesForGrades(){
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse("0,5 * A + 0,5 * B");
        assertThatThrownBy(()-> courseFormula.getGrade(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No grades provided for the course.");
        Map<String,Double> values1 = Map.of();
        assertThatThrownBy(()-> courseFormula.getGrade(values1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No grades provided for the course.");
    }

    @Test
    public void testThrownExceptions_WithNonMatchingValuesForGrades(){
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse("0,5 * A + 0,5 * B");
        var values2 = Map.of("A", 12.0, "B", 14.0, "C", 16.0);
        assertThatThrownBy(()-> courseFormula.getGrade(values2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Grades provided do not match the course variables.");

        var values3 = Map.of("A", 12.0);
        assertThatThrownBy(()-> courseFormula.getGrade(values3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Grades provided do not match the course variables.");
    }

    @Test
    public void testFormulaVisitorToString(){
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse("max(ET; 0,5 * ET + 0,5 * CC)");
        String formatedFormulaString = new ToStringFormulaVisitor().getString(courseFormula);
        assertThat(formatedFormulaString).isEqualTo("max(ET; 0.5*ET + 0.5*CC)");
    }

    @Test
    public void parseInvalidFormula(){
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        assertThatThrownBy(()-> parser.parse("0,5 ** A + 0,5 * B"));
    }

    @Test
    public void parseInvalidFormula2(){
        AntlrFormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        assertThatThrownBy(()-> parser.parse("aa"))
                .isInstanceOf(FormulaParserException.class)
                .hasMessageStartingWith("Error while parsing formula")
                .hasMessageContaining("position 0")
                .hasMessageContaining("mismatched input 'a'");
    }


}
