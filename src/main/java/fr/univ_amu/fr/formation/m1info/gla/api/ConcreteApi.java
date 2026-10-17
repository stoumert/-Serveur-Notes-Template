package fr.univ_amu.fr.formation.m1info.gla.api;

import com.google.inject.Inject;
import io.javalin.Javalin;

import java.util.Set;

public class ConcreteApi {
    private final Set<Api> apiSet;
    @Inject
    public ConcreteApi(Set<Api> apiSet) {
        this.apiSet = apiSet;
    }
    public void registerAllApi(Javalin app) {
        for (Api api : apiSet) {
            api.registerRoutes(app);
        }
    }
}
