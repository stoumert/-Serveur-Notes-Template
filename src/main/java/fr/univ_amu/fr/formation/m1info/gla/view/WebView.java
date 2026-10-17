package fr.univ_amu.fr.formation.m1info.gla.view;

import fr.univ_amu.fr.formation.m1info.gla.configuration.Http;

public interface WebView extends Http {
    /**
     * Retourne le nom de cette vue
     */
    String getViewName();

}
