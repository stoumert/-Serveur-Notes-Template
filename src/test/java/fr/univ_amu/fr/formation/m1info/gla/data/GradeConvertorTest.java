package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GradeConvertorTest {

    @Test
    void fromDTO() {
        List<Teacher> teachers = new ArrayList<>();
        GradeConvertor gradeConvertor = new GradeConvertor(teachers);
        assertThrows(UnsupportedOperationException.class, () -> gradeConvertor.fromDTO(null));
    }

    @Test
    void toDTO() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "D", "D"));
        teachers.add(new Teacher(2, "E", "E"));

        GradeConvertor gradeConvertor = new GradeConvertor(teachers);
        Map<Course, Double> grades = new HashMap<>();
        grades.put(new Course(1, "A", "B", 2, "D", new Teacher(1, "D", "D")), 1.0);
        grades.put(new Course(2, "G", "G", 2, "D", new Teacher(2, "E", "E")), 10.0);
        GradeDTO dto = gradeConvertor.toDTO(grades);
        assertEquals(2, dto.grades().size());
    }
}