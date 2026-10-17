package fr.univ_amu.fr.formation.m1info.gla.model;

/**
 * Exception for illegal change of an identifier
 */
public class IllegalIdSettingException extends RuntimeException {
    public IllegalIdSettingException(String message) {
        super(message);
    }
}
