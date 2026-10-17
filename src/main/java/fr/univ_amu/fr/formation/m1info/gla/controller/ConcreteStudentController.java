package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.StudentService;

import java.util.List;
import java.util.Map;

public class ConcreteStudentController implements StudentController {

    private final StudentService studentService;

    @Inject
    public ConcreteStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public StudentDTO getStudentById(int id) {
        return studentService.get(id);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentService.getAll();
    }

    @Override
    public List<StudentDTO> findStudent(String name, String surname) {
        return studentService.findStudent(name, surname);
    }

    @Override
    public StudentDTO createStudent(StudentDTO student) {
        return studentService.create(student);
    }

    @Override
    public void updateStudent(StudentDTO student) {
        studentService.update(student);
    }

    @Override
    public void deleteStudent(StudentDTO student) {
        studentService.delete(student);
    }

    @Override
    public GradeDTO getStudentGradeDTO(StudentDTO student) {
        return studentService.getStudentGradeDTO(student);
    }

    @Override
    public void updateStudentMarks(int studentId, int courseId, Map<String, Double> marks) {
        studentService.updateStudentMarks(studentId, courseId, marks);
    }

    @Override
    public List<StudentDTO> findAllStudentsByCourse(int courseId) {
        return studentService.findAllStudentsByCourse(courseId);
    }
}