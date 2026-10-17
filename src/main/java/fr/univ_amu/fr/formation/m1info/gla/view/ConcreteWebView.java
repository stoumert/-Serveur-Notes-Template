package fr.univ_amu.fr.formation.m1info.gla.view;

import com.google.inject.Inject;
import io.javalin.Javalin;

import java.util.Set;

public class ConcreteWebView {
    private final Set<WebView> registeredViews;

    @Inject
    public ConcreteWebView(Set<WebView> registeredViews) {
        this.registeredViews = registeredViews;
    }

    public void registerAllView(Javalin app){
        for(WebView webView : registeredViews){
            webView.registerRoutes(app);
        }
    }

}
