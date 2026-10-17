package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteTeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private ConcreteTeacherController teacherController;

    @Test
    void testGetTeacherById() {
        // Given
        int id = 1;
        TeacherDTO expectedTeacher = new TeacherDTO(id, "John", "Doe");
        given(teacherService.getTeacherById(id)).willReturn(expectedTeacher);
        // When
        TeacherDTO result = teacherController.getTeacherById(id);
        // Then
        assertThat(result).isEqualTo(expectedTeacher);
        then(teacherService).should().getTeacherById(id);
    }

    @Test
    void testGetAllTeachers() {
        // Given
        List<TeacherDTO> expectedTeachers = List.of(
                new TeacherDTO(1, "John", "Doe"),
                new TeacherDTO(2, "Jane", "Smith")
        );
        given(teacherService.getAllTeachers()).willReturn(expectedTeachers);
        // When
        List<TeacherDTO> result = teacherController.getAllTeachers();
        // Then
        assertThat(result).isEqualTo(expectedTeachers);
        then(teacherService).should().getAllTeachers();
    }

    @Test
    void testCreateTeacher() {
        // Given
        TeacherDTO inputTeacher = new TeacherDTO(-1, "John", "Doe");
        TeacherDTO createdTeacher = new TeacherDTO(1, "John", "Doe");
        given(teacherService.createTeacher(inputTeacher)).willReturn(createdTeacher);
        // When
        TeacherDTO result = teacherController.createTeacher(inputTeacher);
        // Then
        assertThat(result).isEqualTo(createdTeacher);
        then(teacherService).should().createTeacher(inputTeacher);
    }

    @Test
    void testUpdateTeacher() {
        // Given
        TeacherDTO teacherToUpdate = new TeacherDTO(1, "John", "Updated");
        // When
        teacherController.updateTeacher(teacherToUpdate);
        // Then
        then(teacherService).should().updateTeacher(teacherToUpdate);
    }

    @Test
    void testDeleteTeacher() {
        // Given
        TeacherDTO teacherToDelete = new TeacherDTO(1, "John", "Doe");
        // When
        teacherController.deleteTeacher(teacherToDelete);
        // Then
        then(teacherService).should().deleteTeacher(teacherToDelete);
    }
}