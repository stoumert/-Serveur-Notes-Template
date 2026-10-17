package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.CourseConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.CourseManager;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCCourseDAO;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CourseRepositoryTest {

    private static final int TEACHER_ID_ANIS = 1;
    private static final int TEACHER_ID_YANIS = 2;
    private static final int COURSE_ID_GLA = 1;
    private static final int COURSE_ID_MATH = 2;
    private static final int COURSE_ID_DELETE = 3;
    private static final int COURSE_ID_GENERATED = 10;
    private static final String TEACHER_NAME_ANIS = "Anis";
    private static final String TEACHER_NAME_YANIS = "Yanis";
    private static final String SURNAME_YAHIA = "Yahia";
    private static final String SURNAME_LIASSINE = "Liassine";
    private static final String COURSE_NAME_GLA = "GLA";
    private static final String COURSE_NAME_MATH = "Math";
    private static final String COURSE_NAME_PYTHON = "Python";
    private static final String COURSE_DESC_PROGRAMMING = "Programming";
    private static final String COURSE_DESC_MATHEMATICS = "Mathematics";
    private static final String FORMULA_TP = "TP";
    private static final String FORMULA_EXAM = "EXAM";
    private static final String FORMULA_TP_EXAM = "TP + EXAM";
    private static final int ECTS_5 = 5;
    private static final int ECTS_4 = 4;
    private static final int ECTS_3 = 3;

    @Mock private CourseManager courseManager;
    @Mock private JDBCCourseDAO jdbcDAO;
    @Mock private CourseConvertor convertor;

    private CourseRepository repository;
    private Teacher teacherAnis;
    private Teacher teacherYanis;

    @BeforeEach
    void setUp() throws Exception {
        repository = new CourseRepository();
        teacherAnis = createTeacher(TEACHER_ID_ANIS, TEACHER_NAME_ANIS, SURNAME_YAHIA);
        teacherYanis = createTeacher(TEACHER_ID_YANIS, TEACHER_NAME_YANIS, SURNAME_LIASSINE);

        setField(repository, "courseManager", courseManager);
        setField(repository, "jdbcDAO", jdbcDAO);
        setField(repository, "convertor", convertor);
    }

    @Test
    void testGetById_WhenCourseExists_ReturnsCourse() {
        Course mockCourse = createCourse(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_PROGRAMMING, ECTS_5, FORMULA_TP_EXAM, teacherAnis);
        CourseDTO expectedDTO = createCourseDTO(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_PROGRAMMING, ECTS_5, FORMULA_TP_EXAM, TEACHER_ID_ANIS);
        given(courseManager.get(COURSE_ID_GLA)).willReturn(mockCourse);
        given(convertor.toDTO(mockCourse)).willReturn(expectedDTO);

        Optional<CourseDTO> result = repository.getById(COURSE_ID_GLA);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDTO);
        assertCourseDTODetails(result.get(), COURSE_ID_GLA, COURSE_NAME_GLA);
        then(courseManager).should().get(COURSE_ID_GLA);
        then(convertor).should().toDTO(mockCourse);
    }

    @Test
    void testGetAll_ReturnsAllCourses() {
        Course course1 = createCourse(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_PROGRAMMING, ECTS_5, FORMULA_TP, teacherAnis);
        Course course2 = createCourse(COURSE_ID_MATH, COURSE_NAME_MATH, COURSE_DESC_MATHEMATICS, ECTS_3, FORMULA_EXAM, teacherAnis);
        CourseDTO dto1 = createCourseDTO(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_PROGRAMMING, ECTS_5, FORMULA_TP, TEACHER_ID_ANIS);
        CourseDTO dto2 = createCourseDTO(COURSE_ID_MATH, COURSE_NAME_MATH, COURSE_DESC_MATHEMATICS, ECTS_3, FORMULA_EXAM, TEACHER_ID_ANIS);

        Map<Integer, Course> coursesMap = new HashMap<>();
        coursesMap.put(COURSE_ID_GLA, course1);
        coursesMap.put(COURSE_ID_MATH, course2);

        given(courseManager.getAll()).willReturn(coursesMap);
        given(convertor.toDTO(course1)).willReturn(dto1);
        given(convertor.toDTO(course2)).willReturn(dto2);

        List<CourseDTO> result = repository.getAll();

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(dto1, dto2);
        then(courseManager).should().getAll();
        then(convertor).should().toDTO(course1);
        then(convertor).should().toDTO(course2);
    }

    @Test
    void testCreate_CreatesCourseAndReturnsWithGeneratedId() {
        CourseDTO inputDTO = createCourseDTO(-1, COURSE_NAME_PYTHON, COURSE_DESC_PROGRAMMING, ECTS_4, FORMULA_TP_EXAM, TEACHER_ID_ANIS);
        CourseDTO persistedDTO = inputDTO.clone(COURSE_ID_GENERATED);
        Course createdCourse = createCourse(COURSE_ID_GENERATED, COURSE_NAME_PYTHON, COURSE_DESC_PROGRAMMING, ECTS_4, FORMULA_TP_EXAM, teacherAnis);

        given(jdbcDAO.create(inputDTO)).willReturn(COURSE_ID_GENERATED);
        given(convertor.fromDTO(persistedDTO)).willReturn(createdCourse);

        CourseDTO result = repository.create(inputDTO);

        assertThat(result).isNotNull();
        assertCourseDTODetails(result, COURSE_ID_GENERATED, COURSE_NAME_PYTHON);
        assertThat(result.credits()).isEqualTo(ECTS_4);
        then(jdbcDAO).should().create(inputDTO);
        then(courseManager).should().put(COURSE_ID_GENERATED, createdCourse);
        then(convertor).should().fromDTO(persistedDTO);
    }

    @Test
    void testUpdate_UpdatesCourseInDatabaseAndCache() {
        String updatedName = "Updated GLA";
        String updatedDesc = "Updated course";
        int updatedEcts = 6;
        String updatedFormula = "NEW FORMULA";

        CourseDTO updateDTO = createCourseDTO(COURSE_ID_GLA, updatedName, updatedDesc, updatedEcts, updatedFormula, TEACHER_ID_ANIS);
        Course updatedCourse = createCourse(COURSE_ID_GLA, updatedName, updatedDesc, updatedEcts, updatedFormula, teacherAnis);
        given(convertor.fromDTO(updateDTO)).willReturn(updatedCourse);

        repository.update(updateDTO);

        then(jdbcDAO).should().update(updateDTO);
        then(courseManager).should().remove(COURSE_ID_GLA);
        then(convertor).should().fromDTO(updateDTO);
        then(courseManager).should().put(COURSE_ID_GLA, updatedCourse);
    }

    @Test
    void testDelete_DeletesCourseFromDatabaseAndCache() {
        CourseDTO deleteDTO = createCourseDTO(COURSE_ID_DELETE, "ToDelete", "Course to remove", 2, "F", TEACHER_ID_ANIS);

        repository.delete(deleteDTO);

        then(jdbcDAO).should().delete(deleteDTO);
        then(courseManager).should().remove(COURSE_ID_DELETE);
    }

    @Test
    void testSetTeachersList_UpdatesConvertor() {
        List<Teacher> teachers = List.of(teacherAnis, teacherYanis);
        given(jdbcDAO.getAll()).willReturn(List.of());

        repository.setTeachersList(teachers);

        then(jdbcDAO).should().getAll();
    }

    private Teacher createTeacher(int id, String name, String surname) {
        return new Teacher(id, name, surname);
    }

    private Course createCourse(int id, String name, String description, int ects, String formula, Teacher teacher) {
        return new Course(id, name, description, ects, formula, teacher);
    }

    private CourseDTO createCourseDTO(int id, String name, String description, int ects, String formula, int teacherId) {
        return new CourseDTO(id, name, description, ects, formula, teacherId);
    }

    private void assertCourseDTODetails(CourseDTO course, int expectedId, String expectedName) {
        assertThat(course.id()).isEqualTo(expectedId);
        assertThat(course.name()).isEqualTo(expectedName);
    }

    // Injection des mocks via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }


}