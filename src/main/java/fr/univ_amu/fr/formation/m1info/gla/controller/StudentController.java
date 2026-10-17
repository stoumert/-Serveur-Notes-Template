package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;

import java.util.List;
import java.util.Map;

public interface StudentController {
    StudentDTO getStudentById(int id);
    List<StudentDTO> getAllStudents();
    List<StudentDTO> findStudent(String name, String surname);
    StudentDTO createStudent(StudentDTO student);
    void updateStudent(StudentDTO student);
    void deleteStudent(StudentDTO id);
    GradeDTO getStudentGradeDTO(StudentDTO student);
    void updateStudentMarks(int studentId, int courseId, Map<String, Double> marks);
    List<StudentDTO> findAllStudentsByCourse(int courseId);
}