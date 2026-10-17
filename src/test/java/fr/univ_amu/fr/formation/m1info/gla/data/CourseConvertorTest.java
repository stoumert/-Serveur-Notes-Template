package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseConvertorTest {

    @Test
    void fromDTO() {
        Teacher t = new Teacher(1, "T", "A");
        Course c = new Course(1, "T", "A", 3, "F", t);
        CourseConvertor convertor = new CourseConvertor(List.of(t));
        CourseDTO dto = convertor.toDTO(c);
        assertEquals(1,  dto.id());
        assertEquals("T", dto.name());
        assertEquals("A", dto.description());
        assertEquals(3, dto.credits());
        assertEquals("F", dto.formula());
        assertEquals(1, dto.supervisorId());
    }

    @Test
    void toDTO() {
        Teacher t = new Teacher(1, "T", "A");
        CourseDTO cdto = new CourseDTO(1, "T", "A", 3, "F", 1);
        CourseConvertor convertor = new CourseConvertor(List.of(t));
        Course c = convertor.fromDTO(cdto);
        assertEquals(1,  c.getId());
        assertEquals("T", c.getName());
        assertEquals("A", c.getDescription());
        assertEquals(3, c.getCredits());
        assertEquals("F", c.getFormula());
        assertEquals(1, c.getSupervisor().getId());
    }
}