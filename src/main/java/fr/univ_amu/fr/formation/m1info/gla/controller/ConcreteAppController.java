package fr.univ_amu.fr.formation.m1info.gla.controller;
import com.google.inject.Inject;

public class ConcreteAppController implements AppController{
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final CourseController courseController;

    @Inject
    public ConcreteAppController(StudentController studentController,
                                 TeacherController teacherController,
                                 CourseController courseController) {
        this.studentController = studentController;
        this.teacherController = teacherController;
        this.courseController = courseController;
    }

    @Override
    public CourseController getCoursController() {
        return courseController;
    }

    @Override
    public StudentController getStudentController() {
        return studentController;
    }

    @Override
    public TeacherController getTeacherController() {
        return teacherController;
    }
}
