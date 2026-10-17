package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.TeacherConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.model.TeacherManager;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCTeacherDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherRepository {
    private final JDBCTeacherDAO jdbcDAO;
    private final TeacherManager teacherManager;
    private final TeacherConvertor convertor;

    public TeacherRepository() {
        this.jdbcDAO = new JDBCTeacherDAO();
        this.teacherManager = new TeacherManager();
        this.convertor = new TeacherConvertor();
        loadTeachers();
    }

    private void loadTeachers() {
        List<TeacherDTO> teachersDTO = jdbcDAO.getAll();
        teachersDTO.forEach(teacherDTO -> {
            Teacher teacher = convertor.fromDTO(teacherDTO);
            teacherManager.put(teacher.getId(), teacher);
        });
    }

    public Optional<TeacherDTO> getById(Integer id) {
        return Optional.of(convertor.toDTO(teacherManager.get(id)));
    }

    public List<TeacherDTO> getAll() {
        return new ArrayList<>(teacherManager.getAll().values().stream().map(convertor::toDTO).toList());
    }

    public TeacherDTO create(TeacherDTO teacher) {

        Integer id = jdbcDAO.create(teacher);
        Teacher createdTeacher = new Teacher(id, teacher.name(), teacher.surname());
        teacherManager.put(id, createdTeacher);
        return convertor.toDTO(createdTeacher);
    }

    public void update(TeacherDTO teacher) {
        if (!teacherManager.getAll().containsKey(teacher.id())) {
            throw new IllegalArgumentException("Teacher with id " + teacher.id() + " not found");
        }

        jdbcDAO.update(teacher);
        teacherManager.put(teacher.id(), convertor.fromDTO(teacher));
    }

    public void delete(TeacherDTO teacher) {
        jdbcDAO.delete(teacher);
        teacherManager.remove(teacher.id());
    }
}