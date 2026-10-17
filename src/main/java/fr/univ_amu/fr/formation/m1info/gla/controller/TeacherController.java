package fr.univ_amu.fr.formation.m1info.gla.controller;

import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;

import java.util.List;

public interface TeacherController {
    TeacherDTO getTeacherById(int id);
    List<TeacherDTO> getAllTeachers();
    TeacherDTO createTeacher(TeacherDTO teacher);
    void updateTeacher(TeacherDTO teacher);
    void deleteTeacher(TeacherDTO id);
}
