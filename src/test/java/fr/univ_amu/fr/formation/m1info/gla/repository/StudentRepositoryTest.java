package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.StudentConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Student;
import fr.univ_amu.fr.formation.m1info.gla.model.StudentManager;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCStudentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    private static final int STUDENT_ID_ANIS = 1;
    private static final int STUDENT_ID_YANIS = 2;
    private static final int STUDENT_ID_SAID = 42;
    private static final int STUDENT_ID_DELETE = 5;
    private static final String NAME_ANIS = "Anis";
    private static final String NAME_YANIS = "Yanis";
    private static final String NAME_SAID = "Said";
    private static final String SURNAME_YAHIA = "Yahia";
    private static final String SURNAME_LIASSINE = "Liassine";
    private static final String SURNAME_TOUMERT = "Toumert";

    @Mock private StudentManager studentManager;
    @Mock private JDBCStudentDAO jdbcDAO;
    @Mock private StudentConvertor convertor;

    private StudentRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new StudentRepository();

        setField(repository, "studentManager", studentManager);
        setField(repository, "jdbcDAO", jdbcDAO);
        setField(repository, "convertor", convertor);
    }

    @Test
    void testGetById_WhenStudentExists_ReturnsStudent() {
        Student mockStudent = createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        StudentDTO expectedDTO = createStudentDTO(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        given(studentManager.get(STUDENT_ID_ANIS)).willReturn(mockStudent);
        given(convertor.toDTO(mockStudent)).willReturn(expectedDTO);

        Optional<StudentDTO> result = repository.getById(STUDENT_ID_ANIS);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDTO);
        assertStudentDTODetails(result.get(), STUDENT_ID_ANIS, NAME_ANIS);
        then(studentManager).should().get(STUDENT_ID_ANIS);
        then(convertor).should().toDTO(mockStudent);
    }

    @Test
    void testGetAll_ReturnsAllStudents() {
        Student student1 = createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        Student student2 = createStudent(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);
        StudentDTO dto1 = createStudentDTO(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        StudentDTO dto2 = createStudentDTO(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE);

        Map<Integer, Student> studentsMap = new HashMap<>();
        studentsMap.put(STUDENT_ID_ANIS, student1);
        studentsMap.put(STUDENT_ID_YANIS, student2);

        given(studentManager.getAll()).willReturn(studentsMap);
        given(convertor.toDTO(student1)).willReturn(dto1);
        given(convertor.toDTO(student2)).willReturn(dto2);

        List<StudentDTO> result = repository.getAll();

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(dto1, dto2);
        then(studentManager).should().getAll();
        then(convertor).should().toDTO(student1);
        then(convertor).should().toDTO(student2);
    }

    @Test
    void testCreate_CreatesStudentAndReturnsWithGeneratedId() {
        StudentDTO inputDTO = createStudentDTO(-1, NAME_SAID, SURNAME_TOUMERT);
        Student createdStudent = createStudent(STUDENT_ID_SAID, NAME_SAID, SURNAME_TOUMERT);
        StudentDTO outputDTO = createStudentDTO(STUDENT_ID_SAID, NAME_SAID, SURNAME_TOUMERT);

        given(jdbcDAO.create(inputDTO)).willReturn(STUDENT_ID_SAID);
        given(convertor.fromDTO(any(StudentDTO.class))).willReturn(createdStudent);
        given(convertor.toDTO(createdStudent)).willReturn(outputDTO);

        StudentDTO result = repository.create(inputDTO);

        assertThat(result).isNotNull();
        assertStudentDTODetails(result, STUDENT_ID_SAID, NAME_SAID);
        assertThat(result.surname()).isEqualTo(SURNAME_TOUMERT);
        then(jdbcDAO).should().create(inputDTO);
        then(studentManager).should().put(STUDENT_ID_SAID, createdStudent);
        then(convertor).should().fromDTO(any(StudentDTO.class));
        then(convertor).should().toDTO(createdStudent);
    }

    @Test
    void testUpdate_UpdatesStudentInDatabaseAndCache() {
        StudentDTO updateDTO = createStudentDTOWithCourses(STUDENT_ID_ANIS, "Updated", "Name", List.of(1, 2));
        Student updatedStudent = createStudent(STUDENT_ID_ANIS, "Updated", "Name");
        given(convertor.fromDTO(updateDTO)).willReturn(updatedStudent);

        repository.update(updateDTO);

        then(jdbcDAO).should().update(updateDTO);
        then(studentManager).should().remove(STUDENT_ID_ANIS);
        then(convertor).should().fromDTO(updateDTO);
        then(studentManager).should().put(STUDENT_ID_ANIS, updatedStudent);
    }

    @Test
    void testDelete_DeletesStudentFromDatabaseAndCache() {
        StudentDTO deleteDTO = createStudentDTO(STUDENT_ID_DELETE, "ToDelete", "User");

        repository.delete(deleteDTO);

        then(jdbcDAO).should().delete(deleteDTO);
        then(studentManager).should().remove(STUDENT_ID_DELETE);
    }

    @Test
    void testSetCourses_UpdatesConvertor() {
        List<Course> courses = List.of();

        repository.setCourses(courses);
    }

    private Student createStudent(int id, String name, String surname) {
        return new Student(id, name, surname, List.of(), new HashMap<>());
    }

    private StudentDTO createStudentDTO(int id, String name, String surname) {
        return new StudentDTO(id, name, surname, List.of(), Map.of());
    }

    private StudentDTO createStudentDTOWithCourses(int id, String name, String surname, List<Integer> courseIds) {
        return new StudentDTO(id, name, surname, courseIds, Map.of());
    }

    private void assertStudentDTODetails(StudentDTO student, int expectedId, String expectedName) {
        assertThat(student.id()).isEqualTo(expectedId);
        assertThat(student.name()).isEqualTo(expectedName);
    }

    // Injection des mocks via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}