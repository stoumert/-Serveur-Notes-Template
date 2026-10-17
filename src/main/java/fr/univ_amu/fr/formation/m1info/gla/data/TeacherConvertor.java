package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;

public class TeacherConvertor implements Convertor<Teacher, TeacherDTO> {
    @Override
    public Teacher fromDTO(TeacherDTO dto) {
        return new Teacher(dto.id(), dto.name(), dto.surname());
    }

    @Override
    public TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getId(), teacher.getName(), teacher.getSurname());
    }
}
