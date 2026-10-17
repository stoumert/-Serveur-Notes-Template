package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;

import java.util.List;
import java.util.Optional;

public class CourseConvertor implements Convertor<Course, CourseDTO> {

    private final List<Teacher> teachers;

    public  CourseConvertor(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public Course fromDTO(CourseDTO dto) {
        Optional<Teacher> teacherOptional = teachers.stream().filter(e -> e.getId() == dto.supervisorId()).findFirst();
        if (teacherOptional.isEmpty()) {
            throw new UnknownTeacherException("No teacher with id " + dto.supervisorId() + " found");
        }
        Teacher teacher =  teacherOptional.get();
        return new Course(dto.id(), dto.name(), dto.description(), dto.credits(), dto.formula(), teacher);
    }

    @Override
    public CourseDTO toDTO(Course course) {
        return new CourseDTO(course.getId(), course.getName(), course.getDescription(),
                course.getCredits(), course.getFormula(), course.getSupervisor().getId());
    }
}
