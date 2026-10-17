package fr.univ_amu.fr.formation.m1info.gla.storage;

public interface IdentifierFactory<K> {
    /**
     * Give the next ID
     * @return an unique identifier
     */
    K next();
}
