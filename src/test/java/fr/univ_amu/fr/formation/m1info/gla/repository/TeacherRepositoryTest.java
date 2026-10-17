package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.TeacherConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.model.TeacherManager;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCTeacherDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockConstruction;

@ExtendWith(MockitoExtension.class)
class TeacherRepositoryTest {

    private static final int TEACHER_ID_ANIS = 1;
    private static final int TEACHER_ID_YANIS = 2;
    private static final int TEACHER_ID_SAID = 15;
    private static final int TEACHER_ID_DELETE = 7;
    private static final int TEACHER_ID_NOT_FOUND = 999;
    private static final String NAME_ANIS = "Anis";
    private static final String NAME_YANIS = "Yanis";
    private static final String NAME_SAID = "Said";
    private static final String SURNAME_YAHIA = "Yahia";
    private static final String SURNAME_LIASSINE = "Liassine";
    private static final String SURNAME_TOUMERT = "Toumert";

    @Mock private TeacherManager teacherManager;
    @Mock private JDBCTeacherDAO jdbcDAO;
    @Mock private TeacherConvertor convertor;

    private TeacherRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        // On mock la construction du DAO pour éviter d'appeler la vraie DB
        // Le constructeur de TeacherRepository appelle loadTeachers() qui fait un jdbcDAO.getAll()
        try (MockedConstruction<JDBCTeacherDAO> mockedDAO = mockConstruction(JDBCTeacherDAO.class,
                (mock, context) -> given(mock.getAll()).willReturn(List.of()))) {
            repository = new TeacherRepository();
        }

        // On injecte nos mocks après la création
        setField(repository, "teacherManager", teacherManager);
        setField(repository, "jdbcDAO", jdbcDAO);
        setField(repository, "convertor", convertor);
    }

    @Test
    void testGetById_WhenTeacherExists_ReturnsTeacher() {
        Teacher mockTeacher = createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        TeacherDTO expectedDTO = createTeacherDTO(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        given(teacherManager.get(TEACHER_ID_ANIS)).willReturn(mockTeacher);
        given(convertor.toDTO(mockTeacher)).willReturn(expectedDTO);

        Optional<TeacherDTO> result = repository.getById(TEACHER_ID_ANIS);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDTO);
        assertTeacherDTODetails(result.get(), TEACHER_ID_ANIS, NAME_ANIS);
        then(teacherManager).should().get(TEACHER_ID_ANIS);
        then(convertor).should().toDTO(mockTeacher);
    }

    @Test
    void testGetAll_ReturnsAllTeachers() {
        Teacher teacher1 = createTeacher(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        Teacher teacher2 = createTeacher(TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);
        TeacherDTO dto1 = createTeacherDTO(TEACHER_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        TeacherDTO dto2 = createTeacherDTO(TEACHER_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);

        Map<Integer, Teacher> teachersMap = new HashMap<>();
        teachersMap.put(TEACHER_ID_ANIS, teacher1);
        teachersMap.put(TEACHER_ID_YANIS, teacher2);

        given(teacherManager.getAll()).willReturn(teachersMap);
        given(convertor.toDTO(teacher1)).willReturn(dto1);
        given(convertor.toDTO(teacher2)).willReturn(dto2);

        List<TeacherDTO> result = repository.getAll();

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(dto1, dto2);
        then(teacherManager).should().getAll();
    }

    @Test
    void testCreate_CreatesTeacherAndReturnsWithGeneratedId() {
        TeacherDTO inputDTO = createTeacherDTO(-1, NAME_SAID, SURNAME_TOUMERT);
        TeacherDTO outputDTO = createTeacherDTO(TEACHER_ID_SAID, NAME_SAID, SURNAME_TOUMERT);

        given(jdbcDAO.create(inputDTO)).willReturn(TEACHER_ID_SAID);
        // On utilise any(Teacher.class) car le code crée un nouvel objet Teacher
        given(convertor.toDTO(any(Teacher.class))).willReturn(outputDTO);

        TeacherDTO result = repository.create(inputDTO);

        assertThat(result).isNotNull();
        assertTeacherDTODetails(result, TEACHER_ID_SAID, NAME_SAID);
        assertThat(result.surname()).isEqualTo(SURNAME_TOUMERT);
        then(jdbcDAO).should().create(inputDTO);
        then(teacherManager).should().put(eq(TEACHER_ID_SAID), any(Teacher.class));
    }

    @Test
    void testUpdate_WhenTeacherExists_UpdatesTeacher() {
        String updatedName = "Updated";
        String updatedSurname = "Name";

        TeacherDTO updateDTO = createTeacherDTO(TEACHER_ID_ANIS, updatedName, updatedSurname);
        Teacher updatedTeacher = createTeacher(TEACHER_ID_ANIS, updatedName, updatedSurname);

        Map<Integer, Teacher> teachersMap = new HashMap<>();
        teachersMap.put(TEACHER_ID_ANIS, createTeacher(TEACHER_ID_ANIS, "Old", "Name"));

        given(teacherManager.getAll()).willReturn(teachersMap);
        given(convertor.fromDTO(updateDTO)).willReturn(updatedTeacher);

        repository.update(updateDTO);

        then(jdbcDAO).should().update(updateDTO);
        then(teacherManager).should().put(TEACHER_ID_ANIS, updatedTeacher);
        then(convertor).should().fromDTO(updateDTO);
    }

    @Test
    void testUpdate_WhenTeacherNotFound_ThrowsException() {
        TeacherDTO updateDTO = createTeacherDTO(TEACHER_ID_NOT_FOUND, "NonExistent", "Teacher");
        Map<Integer, Teacher> emptyMap = new HashMap<>();
        given(teacherManager.getAll()).willReturn(emptyMap);

        assertThatThrownBy(() -> repository.update(updateDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Teacher with id " + TEACHER_ID_NOT_FOUND + " not found");

        then(jdbcDAO).shouldHaveNoInteractions();
    }

    @Test
    void testDelete_DeletesTeacherFromDatabaseAndCache() {
        TeacherDTO deleteDTO = createTeacherDTO(TEACHER_ID_DELETE, "ToDelete", "Teacher");

        repository.delete(deleteDTO);

        then(jdbcDAO).should().delete(deleteDTO);
        then(teacherManager).should().remove(TEACHER_ID_DELETE);
    }

    private Teacher createTeacher(int id, String name, String surname) {
        return new Teacher(id, name, surname);
    }

    private TeacherDTO createTeacherDTO(int id, String name, String surname) {
        return new TeacherDTO(id, name, surname);
    }

    private void assertTeacherDTODetails(TeacherDTO teacher, int expectedId, String expectedName) {
        assertThat(teacher.id()).isEqualTo(expectedId);
        assertThat(teacher.name()).isEqualTo(expectedName);
    }

    // Injection des mocks via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }


}