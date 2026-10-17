package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteStudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ConcreteStudentController studentController;

    @Test
    void testGetStudentById() {
        // Given
        int id = 1;
        StudentDTO expectedStudent = new StudentDTO(id, "Alice", "W", List.of(), Map.of());
        given(studentService.get(id)).willReturn(expectedStudent);
        // When
        StudentDTO result = studentController.getStudentById(id);
        // Then
        assertThat(result).isEqualTo(expectedStudent);
        then(studentService).should().get(id);
    }

    @Test
    void testGetAllStudents() {
        // Given
        List<StudentDTO> expectedStudents = List.of(
                new StudentDTO(1, "Alice", "W", List.of(), Map.of()),
                new StudentDTO(2, "Bob", "M", List.of(), Map.of())
        );
        given(studentService.getAll()).willReturn(expectedStudents);
        // When
        List<StudentDTO> result = studentController.getAllStudents();
        // Then
        assertThat(result).isEqualTo(expectedStudents);
        then(studentService).should().getAll();
    }

    @Test
    void testFindStudent() {
        // Given
        String name = "Alice";
        String surname = "W";
        List<StudentDTO> expectedStudents = List.of(new StudentDTO(1, name, surname, List.of(), Map.of()));
        given(studentService.findStudent(name, surname)).willReturn(expectedStudents);
        // When
        List<StudentDTO> result = studentController.findStudent(name, surname);
        // Then
        assertThat(result).isEqualTo(expectedStudents);
        then(studentService).should().findStudent(name, surname);
    }

    @Test
    void testCreateStudent() {
        // Given
        StudentDTO inputStudent = new StudentDTO(-1, "Alice", "W", List.of(), Map.of());
        StudentDTO createdStudent = new StudentDTO(1, "Alice", "W", List.of(), Map.of());
        given(studentService.create(inputStudent)).willReturn(createdStudent);
        // When
        StudentDTO result = studentController.createStudent(inputStudent);
        // Then
        assertThat(result).isEqualTo(createdStudent);
        then(studentService).should().create(inputStudent);
    }

    @Test
    void testUpdateStudent() {
        // Given
        StudentDTO studentToUpdate = new StudentDTO(1, "Alice", "Updated", List.of(), Map.of());
        // When
        studentController.updateStudent(studentToUpdate);
        // Then
        then(studentService).should().update(studentToUpdate);
    }

    @Test
    void testDeleteStudent() {
        // Given
        StudentDTO studentToDelete = new StudentDTO(1, "Alice", "W", List.of(), Map.of());
        // When
        studentController.deleteStudent(studentToDelete);
        // Then
        then(studentService).should().delete(studentToDelete);
    }

    @Test
    void testGetStudentGradeDTO() {
        // Given
        StudentDTO student = new StudentDTO(1, "Alice", "W", List.of(), Map.of());
        GradeDTO expectedGrades = new GradeDTO(Collections.emptyMap());
        given(studentService.getStudentGradeDTO(student)).willReturn(expectedGrades);
        // When
        GradeDTO result = studentController.getStudentGradeDTO(student);
        // Then
        assertThat(result).isEqualTo(expectedGrades);
        then(studentService).should().getStudentGradeDTO(student);
    }

    @Test
    void testUpdateStudentMarks() {
        // Given
        int studentId = 1;
        int courseId = 101;
        Map<String, Double> marks = Map.of("Exam", 15.0);
        // When
        studentController.updateStudentMarks(studentId, courseId, marks);
        // Then
        then(studentService).should().updateStudentMarks(studentId, courseId, marks);
    }

    @Test
    void findAllStudentsByCourse() {
        // Given
        int courseId = 101;
        List<StudentDTO> expectedStudents = List.of(new StudentDTO(1, "Alice", "W",
                List.of(courseId), Map.of()));
        given(studentService.findAllStudentsByCourse(courseId)).willReturn(expectedStudents);
        // When
        List<StudentDTO> result = studentController.findAllStudentsByCourse(courseId);
        // Then
        assertThat(result).isEqualTo(expectedStudents);
        then(studentService).should().findAllStudentsByCourse(courseId);
    }
}