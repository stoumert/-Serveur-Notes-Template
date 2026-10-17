package fr.univ_amu.fr.formation.m1info.gla.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void getId() {
        Teacher t = new Teacher(-1, "toto", "titi");
        assertEquals(-1, t.getId());
    }

    @Test
    void setId() {
        Teacher t = new Teacher(-1, "toto", "titi");
        t.setId(2);
        assertEquals(2, t.getId());
        assertThrows(IllegalIdSettingException.class, () -> t.setId(3));
    }

    @Test
    void getName() {
        Teacher t = new Teacher(-1, "toto", "titi");
        assertEquals("toto", t.getName());
    }

    @Test
    void getSurname() {
        Teacher t = new Teacher(-1, "toto", "titi");
        assertEquals("titi", t.getSurname());
    }
}