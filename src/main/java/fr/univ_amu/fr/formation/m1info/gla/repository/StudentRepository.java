package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.*;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Student;
import fr.univ_amu.fr.formation.m1info.gla.model.StudentManager;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCStudentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {

    private final StudentManager studentManager;
    private final JDBCStudentDAO jdbcDAO;
    private StudentConvertor convertor;

    public StudentRepository() {
        this.studentManager = new StudentManager();
        this.jdbcDAO = new JDBCStudentDAO();
    }
    private void loadData() {
        List<StudentDTO> studentsDTO = jdbcDAO.getAll();
        studentsDTO.forEach(studentDTO -> {
            Student student = convertor.fromDTO(studentDTO);
            studentManager.put(studentDTO.id(), student);
        });
    }

    public Optional<StudentDTO> getById(Integer id) {
        return Optional.of(convertor.toDTO(studentManager.get(id)));
    }

    public List<StudentDTO> getAll() {
        return new ArrayList<>(studentManager.getAll().values().stream().map(convertor::toDTO).toList());
    }

    public StudentDTO create(StudentDTO student) {
        Integer id = jdbcDAO.create(student);

        Student createdStudent = convertor.fromDTO(student.clone(id));

        studentManager.put(id, createdStudent);
        return convertor.toDTO(createdStudent);
    }
    public void update(StudentDTO student) {
        jdbcDAO.update(student);
        studentManager.remove(student.id());
        studentManager.put(student.id(), convertor.fromDTO(student));
    }

    public void delete(StudentDTO student) {
        jdbcDAO.delete(student);
        studentManager.remove(student.id());
    }

    public void setCourses(List<Course> courses) {
        this.convertor = new StudentConvertor(courses);
        loadData();
    }
    public void update(StudentDTO student, List<Course> courses) {
        jdbcDAO.update(student);
        StudentConvertor studentConvertor = new StudentConvertor(courses) ;
        studentManager.remove(student.id());
        studentManager.put(student.id(), studentConvertor.fromDTO(student));
    }
}

//    public Optional<StudentDTO> getByIdAsDTO(Integer id) {
//        return getById(id).map(convertor::toDTO);
//    }
//
//    public List<StudentDTO> getAllAsDTO() {
//        return students.stream().map(convertor::toDTO).toList();
//    }
//
//    public StudentDTO createFromDTO(StudentDTO studentDTO) {
//        Student student = convertor.fromDTO(studentDTO);
//        Student created = create(student);
//        return convertor.toDTO(created);
//    }
//
//    public void updateFromDTO(StudentDTO studentDTO) {
//        Student student = convertor.fromDTO(studentDTO);
//        update(student);
//    }
//
//    public void deleteFromDTO(StudentDTO studentDTO) {
//        Student student = convertor.fromDTO(studentDTO);
//        delete(student);
//    }
