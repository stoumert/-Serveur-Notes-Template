package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.configuration.AppConfig;
import fr.univ_amu.fr.formation.m1info.gla.api.ConcreteApi;
import fr.univ_amu.fr.formation.m1info.gla.view.ConcreteWebView;

import io.javalin.Javalin;

public class Controller {
    private final ConcreteWebView concreteWebView;
    private final ConcreteApi concreteApi;

    @Inject
    public Controller(ConcreteWebView concreteWebView, ConcreteApi concreteApi) {
        this.concreteWebView = concreteWebView;
        this.concreteApi = concreteApi;
    }
    public void start() {
        Javalin app = AppConfig.initJavalin();
        concreteWebView.registerAllView(app);
        concreteApi.registerAllApi(app);
        app.start();
    }
}