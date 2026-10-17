package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;

import java.util.List;
import java.util.Set;

public interface CourseController {
    CourseDTO getCourseById(int id);
    List<CourseDTO> getAllCourses();
    CourseDTO createCourse(CourseDTO course);
    void updateCourse(CourseDTO course);
    void deleteCourse(CourseDTO id);
    List<CourseDTO> getCoursesByName(String name);
    Set<String> getVariableCourseFormula(CourseDTO course);
}
