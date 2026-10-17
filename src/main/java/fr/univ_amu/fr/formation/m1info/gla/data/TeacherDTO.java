package fr.univ_amu.fr.formation.m1info.gla.data;

import fr.univ_amu.fr.formation.m1info.gla.storage.Identifiable;

public record TeacherDTO(Integer id, String name, String surname)
        implements ClonableWithNewID<Integer, TeacherDTO>, Identifiable<Integer> {
    @Override
    public TeacherDTO clone(Integer newID) {
        return new TeacherDTO(newID, this.name, this.surname);
    }
}
