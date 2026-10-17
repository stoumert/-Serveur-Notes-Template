package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.storage.Identifiable;

import java.util.List;
import java.util.Map;

public record StudentDTO(Integer id, String name, String surname, List<Integer> courses,
                         Map<Integer, Map<String, Double>> marks)
        implements ClonableWithNewID<Integer, StudentDTO>, Identifiable<Integer> {

    @Override
    public StudentDTO clone(Integer newID) {
        return new  StudentDTO(newID, this.name, this.surname, this.courses, this.marks);
    }
}
