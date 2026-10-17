package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeConvertor implements Convertor<Map<Course, Double>, GradeDTO> {

    private final List<Teacher> teachers;

    public GradeConvertor(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public Map<Course, Double> fromDTO(GradeDTO dto) {
        throw new UnsupportedOperationException("Will not be supported");
    }

    @Override
    public GradeDTO toDTO(Map<Course, Double> element) {
        CourseConvertor convertor = new CourseConvertor(teachers);
        Map<CourseDTO, Double> marksDto = new HashMap<>();
        for (Map.Entry<Course, Double> entry : element.entrySet()) {
            CourseDTO courseDto = convertor.toDTO(entry.getKey());
            marksDto.put(courseDto, entry.getValue());
        }
        return new GradeDTO(marksDto);
    }
}
