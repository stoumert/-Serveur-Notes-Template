package fr.univ_amu.fr.formation.m1info.gla.model.formulas.util;

public class Arrays {
    public static <T> boolean equalsUnordered(T[] array1, T[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        boolean[] matched = new boolean[array1.length];
        for(T item : array1) {
            boolean foundMatch = false;
            for (int j = 0; j < array1.length; j++) {
                if (!matched[j] && item.equals(array2[j])) {
                    matched[j] = true;
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                return false;
            }
        }
        return true;
    }
}
