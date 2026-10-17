package fr.univ_amu.fr.formation.m1info.gla.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceTest {

    private static final int STUDENT_ID_ANIS = 1;
    private static final int STUDENT_ID_YANIS = 2;
    private static final int STUDENT_ID_SAID = 3;
    private static final int COURSE_ID_TEST = 1;
    private static final int COURSE_ID_101 = 101;
    private static final String NAME_ANIS = "Anis";
    private static final String NAME_YANIS = "Yanis";
    private static final String NAME_SAID = "Said";
    private static final String SURNAME_YAHIA = "Yahia";
    private static final String SURNAME_LIASSINE = "Liassine";
    private static final String SURNAME_TOUMERT = "Toumert";

    @Mock private CourseService courseService;
    @Mock private TeacherService teacherService;
    @Mock private StudentRepository studentRepository;

    private StudentService studentService;
    private Teacher mockTeacher;
    private Course mockCourse;

    @BeforeEach
    void setUp() {
        mockTeacher = new Teacher(1, "Prof", "Test");
        mockCourse = new Course(COURSE_ID_TEST, "Test", "Course", 5, "TP", mockTeacher);

        given(courseService.getCourses()).willReturn(List.of(mockCourse));
        given(teacherService.getTeachers()).willReturn(List.of(mockTeacher));

        studentService = new StudentService(courseService, teacherService, studentRepository);
    }

    @Test
    void testGet_WhenStudentExists_ReturnsStudent() {
        StudentDTO expectedStudent = createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        given(studentRepository.getById(STUDENT_ID_ANIS)).willReturn(Optional.of(expectedStudent));

        StudentDTO result = studentService.get(STUDENT_ID_ANIS);

        assertStudentDetails(result, STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        then(studentRepository).should().getById(STUDENT_ID_ANIS);
    }

    @Test
    void testGet_WhenStudentNotFound_ThrowsException() {
        int nonExistentId = 999;
        given(studentRepository.getById(nonExistentId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.get(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void testGetAll_ReturnsAllStudents() {
        List<StudentDTO> expectedStudents = List.of(
                createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA),
                createStudent(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE)
        );
        given(studentRepository.getAll()).willReturn(expectedStudents);

        List<StudentDTO> result = studentService.getAll();

        assertThat(result).hasSize(2).isEqualTo(expectedStudents);
        then(studentRepository).should().getAll();
    }

    @Test
    void testCreate_CreatesAndReturnsStudent() {
        StudentDTO inputStudent = createStudent(-1, NAME_SAID, SURNAME_TOUMERT);
        StudentDTO createdStudent = createStudent(STUDENT_ID_SAID, NAME_SAID, SURNAME_TOUMERT);
        given(studentRepository.create(inputStudent)).willReturn(createdStudent);

        StudentDTO result = studentService.create(inputStudent);

        assertThat(result).isEqualTo(createdStudent);
        assertThat(result.id()).isEqualTo(STUDENT_ID_SAID);
        then(studentRepository).should().create(inputStudent);
    }


    @Test
    void testDelete_DeletesStudent() {
        StudentDTO studentToDelete = createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);

        studentService.delete(studentToDelete);

        then(studentRepository).should().delete(studentToDelete);
    }

    @Test
    void testUpdateStudentMarks_UpdatesMarksForExistingStudent() {
        int courseId = 10;
        Map<Integer, Map<String, Double>> existingMarks = new HashMap<>();
        existingMarks.put(courseId, Map.of("CC", 10.0));

        StudentDTO originalStudent = createStudentWithMarks(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA, List.of(), existingMarks);
        given(studentRepository.getById(STUDENT_ID_ANIS)).willReturn(Optional.of(originalStudent));

        Map<String, Double> newMarks = Map.of("Exam", 15.0, "TP", 12.0);

        studentService.updateStudentMarks(STUDENT_ID_ANIS, courseId, newMarks);

        then(studentRepository).should().getById(STUDENT_ID_ANIS);
    }

    @Test
    void testUpdateStudentMarks_WhenStudentNotFound_ThrowsException() {
        int nonExistentId = 999;
        int courseId = 10;
        Map<String, Double> newMarks = Map.of("Exam", 15.0);
        given(studentRepository.getById(nonExistentId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudentMarks(nonExistentId, courseId, newMarks))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");

        then(studentRepository).should().getById(nonExistentId);
    }

    @Test
    void testFindStudent_FiltersStudentsByNameAndSurname() {
        String searchName = NAME_ANIS;
        String searchSurname = "Yah";

        StudentDTO match = createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA);
        StudentDTO wrongName = createStudent(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_YAHIA);
        StudentDTO wrongSurname = createStudent(STUDENT_ID_SAID, NAME_ANIS, SURNAME_TOUMERT);
        StudentDTO noMatch = createStudent(4, NAME_YANIS, SURNAME_LIASSINE);

        given(studentRepository.getAll()).willReturn(List.of(match, wrongName, wrongSurname, noMatch));

        List<StudentDTO> result = studentService.findStudent(searchName, searchSurname);

        assertThat(result)
                .hasSize(1)
                .containsExactly(match)
                .doesNotContain(wrongName, wrongSurname, noMatch);
        then(studentRepository).should().getAll();
    }

    @Test
    void testFindStudent_WithEmptySearch_ReturnsAllStudents() {
        List<StudentDTO> allStudents = List.of(
                createStudent(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA),
                createStudent(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE)
        );
        given(studentRepository.getAll()).willReturn(allStudents);

        List<StudentDTO> result = studentService.findStudent("", "");

        assertThat(result).hasSize(2).isEqualTo(allStudents);
    }

    @Test
    void testFindAllStudentsByCourse_FiltersStudentsByCourseId() {
        StudentDTO enrolled1 = createStudentWithCourses(STUDENT_ID_ANIS, NAME_ANIS, SURNAME_YAHIA, List.of(COURSE_ID_101, 102));
        StudentDTO enrolled2 = createStudentWithCourses(STUDENT_ID_YANIS, NAME_YANIS, SURNAME_LIASSINE, List.of(COURSE_ID_101));
        StudentDTO notEnrolled = createStudentWithCourses(STUDENT_ID_SAID, NAME_SAID, SURNAME_TOUMERT, List.of(102));

        given(studentRepository.getAll()).willReturn(List.of(enrolled1, enrolled2, notEnrolled));

        List<StudentDTO> result = studentService.findAllStudentsByCourse(COURSE_ID_101);

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(enrolled1, enrolled2)
                .doesNotContain(notEnrolled);
        then(studentRepository).should().getAll();
    }

    @Test
    void testGetStudentGradeDTO_ReturnsGradeDTO() {
        StudentDTO studentDTO = createStudentWithMarks(
                STUDENT_ID_ANIS,
                NAME_ANIS,
                SURNAME_YAHIA,
                List.of(COURSE_ID_TEST),
                Map.of(COURSE_ID_TEST, Map.of("TP", 15.0))
        );

        GradeDTO result = studentService.getStudentGradeDTO(studentDTO);

        assertThat(result).isNotNull();
        assertThat(result.grades()).isNotEmpty();
        assertThat(result.grades().values()).contains(15.0);
    }

    private StudentDTO createStudent(int id, String name, String surname) {
        return new StudentDTO(id, name, surname, List.of(), Map.of());
    }

    private StudentDTO createStudentWithCourses(int id, String name, String surname, List<Integer> courseIds) {
        return new StudentDTO(id, name, surname, courseIds, Map.of());
    }

    private StudentDTO createStudentWithMarks(int id, String name, String surname,
                                              List<Integer> courseIds, Map<Integer, Map<String, Double>> marks) {
        return new StudentDTO(id, name, surname, courseIds, marks);
    }

    private void assertStudentDetails(StudentDTO student, int expectedId, String expectedName, String expectedSurname) {
        assertThat(student.id()).isEqualTo(expectedId);
        assertThat(student.name()).isEqualTo(expectedName);
        assertThat(student.surname()).isEqualTo(expectedSurname);
    }
}