package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Student;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentConvertorTest {

    @Test
    void fromDTO() {
        Teacher t = new Teacher(1, "T", "A");
        Course c = new Course(1, "T", "A", 3, "F", t);
        List<Course> courses = new ArrayList<>();
        courses.add(c);
        Map<Integer, Map<String, Double>> marksDto = new HashMap<>();
        marksDto.put(1, new HashMap<>());
        marksDto.get(1).put("A", 10.0);
        StudentDTO sdto = new StudentDTO(1, "T", "A", List.of(1), marksDto);
        StudentConvertor convertor = new StudentConvertor(courses);
        Student student = convertor.fromDTO(sdto);
        assertEquals(1, student.getId());
        assertEquals("T", student.getName());
        assertEquals("A", student.getSurname());
        assertEquals(1, student.getCourses().size());
        assertEquals(1, student.getMarks().size());
    }

    @Test
    void toDTO() {
        Teacher t = new Teacher(1, "T", "A");
        Course c = new Course(1, "T", "A", 3, "F", t);
        List<Course> courses = new ArrayList<>();
        courses.add(c);
        Map<Course, Map<String, Double>> marks = new HashMap<>();
        marks.put(c, new HashMap<>());
        marks.get(c).put("A", 10.0);
        Student student = new Student(1, "T", "A", courses, marks);
        StudentConvertor studentConvertor = new StudentConvertor(courses);
        StudentDTO dto = studentConvertor.toDTO(student);
        assertEquals(1, dto.id());
        assertEquals("T", dto.name());
        assertEquals("A", dto.surname());
        assertEquals(1, dto.courses().size());
        assertEquals(1, dto.marks().size());
    }
}