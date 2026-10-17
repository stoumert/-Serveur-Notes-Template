package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherConvertorTest {

    @Test
    void fromDTO() {
        Teacher t = new Teacher(1, "T", "A");
        TeacherConvertor teacherConvertor = new TeacherConvertor();
        TeacherDTO dto =  teacherConvertor.toDTO(t);
        assertEquals(1, dto.id());
        assertEquals("T", dto.name());
        assertEquals("A", dto.surname());
    }

    @Test
    void toDTO() {
        TeacherDTO dto = new TeacherDTO(1, "T", "A");
        TeacherConvertor teacherConvertor = new TeacherConvertor();
        Teacher t = teacherConvertor.fromDTO(dto);
        assertEquals(1, t.getId());
        assertEquals("T", t.getName());
        assertEquals("A", t.getSurname());
    }
}