package fr.univ_amu.fr.formation.m1info.gla.data;

import java.util.Map;

public record GradeDTO(Map<CourseDTO, Double> grades) {
}
