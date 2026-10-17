package fr.univ_amu.fr.formation.m1info.gla.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    Student buildStudent() {
        Teacher teacher = new Teacher(1, "A", "B");
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1, "GL", "Génie Logiciel", 3, "CC * 0.5 + 0.5*ET", teacher));
        courses.add(new Course(2, "GLA", "Génie Logiciel Avancé", 3, "ET", teacher));
        return new Student(-1, "T", "O", courses, new HashMap<>());
    }

    @Test
    void computeGrades() {
        Student student = buildStudent();
        Teacher teacher = new Teacher(1, "D", "E");
        student.addMark(student.getCourses().getFirst(), "ET", 12.0);
        student.addMark(student.getCourses().getFirst(), "CC", 10.0);
        student.addMark(new Course(3, "POC", "POCPOC", 3, "ET", teacher),
                "ET", 19.0);
        Map<Course, Double> grades = student.computeGrades();
        assertEquals(1, grades.size());
        assertEquals(11.0, grades.get(student.getCourses().getFirst()), 0.0);
    }

    @Test
    void getMarks() {
        Student student = buildStudent();
        Teacher teacher = new Teacher(1, "D", "E");
        student.addMark(student.getCourses().getFirst(), "CC1", 12.0);
        student.addMark(student.getCourses().getFirst(), "CC2", 10.0);
        student.addMark(new Course(3, "POC", "POCPOC", 3, "ET", teacher),
                "CC", 19.0);
        var marks = student.getMarks();
        assertEquals(1, marks.size());
        assertEquals(12.0, marks.get(student.getCourses().getFirst()).get("CC1"), 0.0);
    }

    @Test
    void getId() {
        Student s = buildStudent();
        assertEquals(-1, s.getId());
    }

    @Test
    void setId() {
        Student s = buildStudent();
        s.setId(2);
        assertEquals(2, s.getId());
        assertThrows(IllegalIdSettingException.class, () -> s.setId(3));
    }

    @Test
    void getName() {
        Student s = buildStudent();
        assertEquals("T", s.getName());
    }

    @Test
    void getSurname() {
        Student s = buildStudent();
        assertEquals("O", s.getSurname());
    }

    @Test
    void getCourses() {
        Student s = buildStudent();
        List<Course> courses = s.getCourses();
        assertEquals(2, courses.size());
        assertEquals(1, courses.getFirst().getId());
    }
}