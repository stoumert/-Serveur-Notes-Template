package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.service.TeacherService;

import java.util.List;

public class ConcreteTeacherController implements TeacherController {

    private final TeacherService teacherService;

    @Inject
    public ConcreteTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public TeacherDTO getTeacherById(int id) {
        return teacherService.getTeacherById(id);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @Override
    public TeacherDTO createTeacher(TeacherDTO teacher) {
        return teacherService.createTeacher(teacher);
    }

    @Override
    public void updateTeacher(TeacherDTO teacher) {
        teacherService.updateTeacher(teacher);
    }

    @Override
    public void deleteTeacher(TeacherDTO teacher) {
        teacherService.deleteTeacher(teacher);
    }
}