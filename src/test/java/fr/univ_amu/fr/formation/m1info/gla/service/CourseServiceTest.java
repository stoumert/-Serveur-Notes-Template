package fr.univ_amu.fr.formation.m1info.gla.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CourseServiceTest {

    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID_GLA = 1;
    private static final int COURSE_ID_NEW = 10;
    private static final String COURSE_NAME_GLA = "GLA";
    private static final String COURSE_NAME_DESIGN = "Design Pattern";
    private static final String COURSE_DESC_SOFTWARE = "Software Engineering";
    private static final String COURSE_DESC_ARCHITECTURE = "Architecture";
    private static final int COURSE_ECTS = 5;
    private static final String FORMULA_TP = "TP";
    private static final String FORMULA_F = "F";

    @Mock private TeacherService teacherService;
    @Mock private CourseRepository courseRepository;

    private CourseService courseService;
    private Teacher mockTeacher;

    @BeforeEach
    void setUp() {
        mockTeacher = new Teacher(TEACHER_ID, "Anis", "Yahia");
        given(teacherService.getTeachers()).willReturn(List.of(mockTeacher));
        courseService = new CourseService(teacherService, courseRepository);
    }

    @Test
    void testGetAllCourses() {
        List<CourseDTO> expected = List.of(
                createCourse(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_SOFTWARE, COURSE_ECTS, FORMULA_TP)
        );
        given(courseRepository.getAll()).willReturn(expected);

        List<CourseDTO> result = courseService.getAllCourses();

        assertThat(result).isEqualTo(expected);
        then(courseRepository).should().getAll();
    }

    @Test
    void testGetCourseById_WhenCourseExists() {
        CourseDTO expected = createCourse(COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_SOFTWARE, COURSE_ECTS, FORMULA_TP);
        given(courseRepository.getById(COURSE_ID_GLA)).willReturn(Optional.of(expected));

        CourseDTO result = courseService.getCourseById(COURSE_ID_GLA);

        assertThat(result).isEqualTo(expected);
        assertCourseDetails(result, COURSE_ID_GLA, COURSE_NAME_GLA, COURSE_DESC_SOFTWARE);
        then(courseRepository).should().getById(COURSE_ID_GLA);
    }

    @Test
    void testGetCourseById_WhenCourseNotFound_ThrowsException() {
        int nonExistentId = 999;
        given(courseRepository.getById(nonExistentId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void testCreateCourse() {
        CourseDTO input = createCourse(-1, COURSE_NAME_DESIGN, COURSE_DESC_ARCHITECTURE, 4, FORMULA_F);
        CourseDTO expected = createCourse(COURSE_ID_NEW, COURSE_NAME_DESIGN, COURSE_DESC_ARCHITECTURE, 4, FORMULA_F);
        given(courseRepository.create(input)).willReturn(expected);

    }

    @Test
    void testUpdateCourse() {
        CourseDTO course = createCourse(COURSE_ID_GLA, "Updated", COURSE_DESC_SOFTWARE, COURSE_ECTS, FORMULA_F);

        courseService.updateCourse(course);

        then(courseRepository).should().update(course);
    }

    @Test
    void testDeleteCourse() {
        CourseDTO course = createCourse(COURSE_ID_GLA, "ToDelete", COURSE_DESC_SOFTWARE, 3, FORMULA_F);

        courseService.deleteCourse(course);

        then(courseRepository).should().delete(course);
    }

    private CourseDTO createCourse(int id, String name, String description, int ects, String formula) {
        return new CourseDTO(id, name, description, ects, formula, TEACHER_ID);
    }

    private void assertCourseDetails(CourseDTO course, int expectedId, String expectedName, String expectedDescription) {
        assertThat(course.id()).isEqualTo(expectedId);
        assertThat(course.name()).isEqualTo(expectedName);
        assertThat(course.description()).isEqualTo(expectedDescription);
    }
}