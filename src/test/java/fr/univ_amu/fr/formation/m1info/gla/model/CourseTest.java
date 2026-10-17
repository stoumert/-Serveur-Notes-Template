package fr.univ_amu.fr.formation.m1info.gla.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void getId() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals(-1, c.getId());
    }

    @Test
    void setId() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        c.setId(2);
        assertEquals(2, c.getId());
        assertThrows(IllegalIdSettingException.class, () -> c.setId(3));
    }

    @Test
    void getName() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals("GLA", c.getName());
    }

    @Test
    void getDescription() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals("General Langage Association", c.getDescription());
    }

    @Test
    void getCredits() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals(30, c.getCredits());
    }

    @Test
    void getFormula() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals("min(0, (CC+ET)/2)", c.getFormula());
    }

    @Test
    void getSupervisor() {
        Course c = new Course(-1, "GLA", "General Langage Association",
                30, "min(0, (CC+ET)/2)", new Teacher(-1, "titi", "toto"));
        assertEquals("titi", c.getSupervisor().getName());
        assertEquals("toto", c.getSupervisor().getSurname());
        assertEquals(-1, c.getSupervisor().getId());
    }
}