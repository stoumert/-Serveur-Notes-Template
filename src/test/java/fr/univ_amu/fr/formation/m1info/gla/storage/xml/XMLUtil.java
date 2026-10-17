package fr.univ_amu.fr.formation.m1info.gla.storage.xml;

public class XMLUtil {
    public static String removeSpacesOutsideTags(String input) {
        return input.replaceAll("\\s+<", "<").replaceAll(">\\s+", ">");
    }
}
