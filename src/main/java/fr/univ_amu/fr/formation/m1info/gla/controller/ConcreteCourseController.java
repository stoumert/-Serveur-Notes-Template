package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.CourseService;

import java.util.List;
import java.util.Set;

public class ConcreteCourseController implements CourseController {

    private final CourseService courseService;

    @Inject
    public ConcreteCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public CourseDTO getCourseById(int id) {
        return courseService.getCourseById(id);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    public List<CourseDTO> findCourse(String name) {
        return courseService.findCourse(name);
    }

    @Override
    public CourseDTO createCourse(CourseDTO course) {
        return courseService.createCourse(course);
    }

    @Override
    public void updateCourse(CourseDTO course) {
        courseService.updateCourse(course);
    }

    @Override
    public void deleteCourse(CourseDTO course) {
        courseService.deleteCourse(course);
    }

    @Override
    public List<CourseDTO> getCoursesByName(String name) {
        return courseService.findCourse(name);
    }

    @Override
    public Set<String> getVariableCourseFormula(CourseDTO course) {
        return courseService.getVariableOfCourseFormula(course);
    }
}