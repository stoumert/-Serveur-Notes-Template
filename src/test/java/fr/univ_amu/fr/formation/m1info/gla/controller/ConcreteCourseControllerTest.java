package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteCourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private ConcreteCourseController courseController;

    @Test
    void testGetCourseById() {
        // Given
        int id = 1;
        CourseDTO expectedCourse = new CourseDTO(id, "Java", "Advanced Java",
                6, "mean", 1);
        given(courseService.getCourseById(id)).willReturn(expectedCourse);
        // When
        CourseDTO result = courseController.getCourseById(id);
        // Then
        assertThat(result).isEqualTo(expectedCourse);
        then(courseService).should().getCourseById(id);
    }

    @Test
    void testGetAllCourses() {
        // Given
        List<CourseDTO> expectedCourses = List.of(
                new CourseDTO(1, "Java", "Intro", 6, "mean", 1),
                new CourseDTO(2, "Web", "HTML/CSS", 3, "mean", 2)
        );
        given(courseService.getAllCourses()).willReturn(expectedCourses);
        // When
        List<CourseDTO> result = courseController.getAllCourses();
        // Then
        assertThat(result).isEqualTo(expectedCourses);
        then(courseService).should().getAllCourses();
    }

    @Test
    void testCreateCourse() {
        // Given
        CourseDTO inputCourse = new CourseDTO(-1, "Java", "Intro",
                6, "mean", 1);
        CourseDTO createdCourse = new CourseDTO(10, "Java", "Intro",
                6, "mean", 1);
        given(courseService.createCourse(inputCourse)).willReturn(createdCourse);
        // When
        CourseDTO result = courseController.createCourse(inputCourse);
        // Then
        assertThat(result).isEqualTo(createdCourse);
        then(courseService).should().createCourse(inputCourse);
    }

    @Test
    void testUpdateCourse() {
        // Given
        CourseDTO courseToUpdate = new CourseDTO(1, "Java Updated", "Intro",
                6, "mean", 1);
        // When
        courseController.updateCourse(courseToUpdate);
        // Then
        then(courseService).should().updateCourse(courseToUpdate);
    }

    @Test
    void testDeleteCourse() {
        // Given
        CourseDTO courseToDelete = new CourseDTO(1, "Java", "Intro",
                6, "mean", 1);
        // When
        courseController.deleteCourse(courseToDelete);
        // Then
        then(courseService).should().deleteCourse(courseToDelete);
    }

    @Test
    void testGetCoursesByName() {
        // Given
        String name = "Java";
        List<CourseDTO> expectedCourses = List.of(new CourseDTO(1, "Java", "Intro",
                6, "mean", 1));
        given(courseService.findCourse(name)).willReturn(expectedCourses);
        // When
        List<CourseDTO> result = courseController.getCoursesByName(name);
        // Then
        assertThat(result).isEqualTo(expectedCourses);
        then(courseService).should().findCourse(name);
    }

    @Test
    void testFindCourse() {
        // Given
        String name = "Web";
        List<CourseDTO> expectedCourses = List.of(new CourseDTO(2, "Web", "Intro",
                3, "mean", 2));
        given(courseService.findCourse(name)).willReturn(expectedCourses);
        // When
        List<CourseDTO> result = courseController.findCourse(name);
        // Then
        assertThat(result).isEqualTo(expectedCourses);
        then(courseService).should().findCourse(name);
    }
}