package fr.univ_amu.fr.formation.m1info.gla.data;

public interface ClonableWithNewID<K, T> {
    /**
     * Clone with a new identifier
     * @param newID the new identifier
     * @return a clone of the object
     */
    T clone(K newID);
}
