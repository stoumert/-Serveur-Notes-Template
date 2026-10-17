package fr.univ_amu.fr.formation.m1info.gla.storage;

/**
 * Mark a instance as identifiable using an id
 * @param <K> the type of the index
 */
public interface Identifiable<K> {
    K id();
}
