package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Student;

import java.util.*;

public class StudentConvertor implements Convertor<Student, StudentDTO> {

    private final List<Course> courses;

    public StudentConvertor(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public Student fromDTO(StudentDTO dto) {
        List<Course> courses = new ArrayList<>();
        Map<Course, Map<String, Double>> marks = new HashMap<>();
        for (Integer courseId: dto.courses()) {
            Optional<Course> courseOptional = this.courses.stream().filter(e -> e.getId() == courseId).findFirst();
            if (courseOptional.isEmpty()) {
                throw new UnknownCourseException("Course with id " + courseId + " not found");
            }
            Course course = courseOptional.get();
            courses.add(course);
            if (dto.marks().containsKey(courseId)) {
                Map<String, Double> course_marks = new HashMap<>();
                for (String s: dto.marks().get(courseId).keySet()) {
                    course_marks.put(s, dto.marks().get(courseId).get(s));
                }
                marks.put(course, course_marks);
            }
        }
        return new Student(dto.id(), dto.name(), dto.surname(), courses, marks);
    }

    @Override
    public StudentDTO toDTO(Student student) {
        List<Integer> coursesDto = new ArrayList<>();
        Map<Integer, Map<String, Double>> marksDto = new HashMap<>();
        var marks = student.getMarks();
        for (Course course: student.getCourses()) {
            coursesDto.add(course.getId());
            if  (marks.containsKey(course))
                marksDto.put(course.getId(), marks.get(course));
        }
        return new StudentDTO(student.getId(), student.getName(), student.getSurname(),
                List.copyOf(coursesDto), marksDto);
    }
}
