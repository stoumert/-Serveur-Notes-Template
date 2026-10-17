package fr.univ_amu.fr.formation.m1info.gla.model.formulas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class ToStringFormulaVisitorTest {
    @Test
    public void testToStringVisitorVariable() {
        Formula var = new VariableFormula("ET");
        ToStringFormulaVisitor toStringVisitor = new ToStringFormulaVisitor();
        assertThat(var.accept(toStringVisitor)).isEqualTo("ET");
    }


}
