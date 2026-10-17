package fr.univ_amu.fr.formation.m1info.gla.configuration;

import fr.univ_amu.fr.formation.m1info.gla.Main;
import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class AppConfig {
    private AppConfig() {}
    public static Javalin initJavalin() {
        try {
            URI templateDir = Objects.requireNonNull(Main.class.getResource("/templates/")).toURI();
            CodeResolver codeResolver = new DirectoryCodeResolver(Path.of(templateDir));
            TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

            return Javalin.create(config -> {
                config.fileRenderer(new JavalinJte(templateEngine));
                config.staticFiles.add("/public", Location.CLASSPATH);
            });
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }
}
