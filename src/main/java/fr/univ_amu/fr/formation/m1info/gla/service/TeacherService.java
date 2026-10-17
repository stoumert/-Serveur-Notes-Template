package fr.univ_amu.fr.formation.m1info.gla.service;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.repository.TeacherRepository;
import java.util.List;

public class TeacherService {
    private final TeacherRepository teacherRepository;

    @Inject
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
    public List<Teacher> getTeachers(){
        TeacherConvertor convertor = new TeacherConvertor();
        return teacherRepository.getAll().stream().map(convertor::fromDTO).toList();

    }
    public TeacherDTO getTeacherById(int id) {
        return teacherRepository.getById(id).orElseThrow();
    }
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.getAll();
    }
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        return teacherRepository.create(teacherDTO);
    }
    public void updateTeacher(TeacherDTO teacherDTO) {
        teacherRepository.update(teacherDTO);
    }
    public void deleteTeacher(TeacherDTO teacherDTO) {
        teacherRepository.delete(teacherDTO);
    }

}
