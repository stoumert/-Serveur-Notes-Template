package fr.univ_amu.fr.formation.m1info.gla.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.controller.Controller;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeacherServiceTest {
    private static final int TEACHER_ID_ANIS = 1;
    private static final int TEACHER_ID_YANIS = 2;
    private static final int TEACHER_ID_SAID = 3;
    private static final int TEACHER_ID_NEW = 5;
    private static final String NAME_ANIS = "Anis";
    private static final String NAME_YANIS = "Yanis";
    private static final String NAME_SAID = "Said";
    private static final String SURNAME_YAHIA = "Yahia";
    private static final String SURNAME_LIASSINE = "Liassine";
    private static final String SURNAME_TOUMERT = "Toumert";

    @Mock private  TeacherRepository teacherRepository;

    private static TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void testGetTeachers_ReturnsAllTeachersAsModelObjects() {
        TeacherDTO dto1 = createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        TeacherDTO dto2 = createTeacher(TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);
        given(teacherRepository.getAll()).willReturn(List.of(dto1, dto2));

        List<Teacher> result = teacherService.getTeachers();

        assertThat(result).hasSize(2);
        assertTeacherModel(result.get(0), TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        assertTeacherModel(result.get(1), TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);
        then(teacherRepository).should().getAll();
    }

    @Test
    void testGetTeacherById_WhenTeacherExists_ReturnsTeacher() {
        TeacherDTO expectedTeacher = createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        given(teacherRepository.getById(TEACHER_ID_ANIS)).willReturn(Optional.of(expectedTeacher));

        TeacherDTO result = teacherService.getTeacherById(TEACHER_ID_ANIS);

        assertThat(result).isEqualTo(expectedTeacher);
        assertTeacherDTODetails(result, TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        then(teacherRepository).should().getById(TEACHER_ID_ANIS);
    }

    @Test
    void testGetTeacherById_WhenTeacherNotFound_ThrowsException() {
        int nonExistentId = 999;
        given(teacherRepository.getById(nonExistentId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.getTeacherById(nonExistentId))
                .isInstanceOf(NoSuchElementException.class);

        then(teacherRepository).should().getById(nonExistentId);
    }

    @Test
    void testGetAllTeachers_ReturnsAllTeacherDTOs() {
        List<TeacherDTO> expectedTeachers = List.of(
                createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA),
                createTeacher(TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE),
                createTeacher(TEACHER_ID_SAID, NAME_SAID, SURNAME_TOUMERT)
        );
        given(teacherRepository.getAll()).willReturn(expectedTeachers);

        List<TeacherDTO> result = teacherService.getAllTeachers();

        assertThat(result).hasSize(3).isEqualTo(expectedTeachers);
        then(teacherRepository).should().getAll();
    }

    @Test
    void testCreateTeacher_CreatesAndReturnsTeacher() {
        TeacherDTO inputTeacher = createTeacher(-1, NAME_SAID, SURNAME_TOUMERT);
        TeacherDTO createdTeacher = createTeacher(TEACHER_ID_NEW, NAME_SAID, SURNAME_TOUMERT);
        given(teacherRepository.create(inputTeacher)).willReturn(createdTeacher);

        TeacherDTO result = teacherService.createTeacher(inputTeacher);

        assertThat(result).isEqualTo(createdTeacher);
        assertTeacherDTODetails(result, TEACHER_ID_NEW, NAME_SAID, SURNAME_TOUMERT);
        then(teacherRepository).should().create(inputTeacher);
    }

    @Test
    void testUpdateTeacher_UpdatesTeacher() {
        TeacherDTO teacherToUpdate = createTeacher(TEACHER_ID_ANIS, "Updated", "Name");

        teacherService.updateTeacher(teacherToUpdate);

        then(teacherRepository).should().update(teacherToUpdate);
    }

    @Test
    void testDeleteTeacher_DeletesTeacher() {
        TeacherDTO teacherToDelete = createTeacher(7, "ToDelete", "Teacher");

        teacherService.deleteTeacher(teacherToDelete);

        then(teacherRepository).should().delete(teacherToDelete);
    }

    @Test
    void testGetAllTeachers_WhenEmpty_ReturnsEmptyList() {
        given(teacherRepository.getAll()).willReturn(List.of());

        List<TeacherDTO> result = teacherService.getAllTeachers();

        assertThat(result).isEmpty();
        then(teacherRepository).should().getAll();
    }

    @Test
    void testGetTeachers_ConvertsAllTeacherDTOsToModelObjects() {
        List<TeacherDTO> dtos = List.of(
                createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA),
                createTeacher(TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE),
                createTeacher(TEACHER_ID_SAID, NAME_SAID, SURNAME_TOUMERT)
        );
        given(teacherRepository.getAll()).willReturn(dtos);

        List<Teacher> result = teacherService.getTeachers();

        assertThat(result).hasSize(3);
        assertTeacherModel(result.get(0), TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        assertTeacherModel(result.get(1), TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);
        assertTeacherModel(result.get(2), TEACHER_ID_SAID, NAME_SAID, SURNAME_TOUMERT);
    }

    private TeacherDTO createTeacher(int id, String name, String surname) {
        return new TeacherDTO(id, name, surname);
    }

    private void assertTeacherDTODetails(TeacherDTO teacher, int expectedId, String expectedName, String expectedSurname) {
        assertThat(teacher.id()).isEqualTo(expectedId);
        assertThat(teacher.name()).isEqualTo(expectedName);
        assertThat(teacher.surname()).isEqualTo(expectedSurname);
    }

    private void assertTeacherModel(Teacher teacher, int expectedId, String expectedName, String expectedSurname) {
        assertThat(teacher.getId()).isEqualTo(expectedId);
        assertThat(teacher.getName()).isEqualTo(expectedName);
        assertThat(teacher.getSurname()).isEqualTo(expectedSurname);
    }
}