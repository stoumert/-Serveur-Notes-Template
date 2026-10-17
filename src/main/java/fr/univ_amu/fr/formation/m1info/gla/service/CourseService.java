package fr.univ_amu.fr.formation.m1info.gla.service;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.*;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.repository.CourseRepository;
import java.util.List;
import java.util.Set;

public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;

    @Inject
    public CourseService(TeacherService teacherService,
                         CourseRepository courseRepository
                         ) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.courseRepository.setTeachersList(teacherService.getTeachers());
    }
    public List<Course> getCourses() {
        CourseConvertor courseConvertor = new CourseConvertor(teacherService.getTeachers());
        return courseRepository.getAll()
                .stream().map(courseConvertor::fromDTO)
                .toList();
    }
    public List<CourseDTO> getAllCourses() {
        return courseRepository.getAll();
    }
    public CourseDTO getCourseById(int id) {
        return courseRepository.getById(id).orElseThrow(()-> new RuntimeException("Course not found"));
    }
    public List<CourseDTO> findCourse(String name) {
        return courseRepository.getAll().stream().filter(e -> e.name().contains(name)).toList();
    }
    public CourseDTO createCourse(CourseDTO courseDTO) {
        List<Teacher> teachers = teacherService.getTeachers();
        return courseRepository.create(courseDTO,teachers);
    }
    public void updateCourse(CourseDTO courseDTO) {
        courseRepository.update(courseDTO);
    }
    public void deleteCourse(CourseDTO courseDTO) {
        courseRepository.delete(courseDTO);
    }
    public Set<String> getVariableOfCourseFormula(CourseDTO courseDTO) {
        CourseConvertor courseConvertor = new CourseConvertor(teacherService.getTeachers());
        return courseConvertor.fromDTO(courseDTO).getViableCourseFormula();
    }
}
