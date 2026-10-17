package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.storage.Identifiable;

public record CourseDTO(Integer id, String name, String description, int credits,
                        String formula, Integer supervisorId)
        implements ClonableWithNewID<Integer, CourseDTO>, Identifiable<Integer> {

    @Override
    public CourseDTO clone(Integer newID) {
        return new CourseDTO(newID, this.name, this.description, this.credits, this.formula, this.supervisorId);
    }
}
