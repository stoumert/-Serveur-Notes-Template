package fr.univ_amu.fr.formation.m1info.gla.configuration;

import io.javalin.Javalin;

public interface Http {
    /**
     * Enregistre les routes spécifiques à cette vue
     * @param app Le framework web Javalin
     */
    void registerRoutes(Javalin app);
}
