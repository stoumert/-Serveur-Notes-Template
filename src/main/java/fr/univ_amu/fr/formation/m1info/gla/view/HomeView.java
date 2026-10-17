package fr.univ_amu.fr.formation.m1info.gla.view;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Objects;


public class HomeView implements WebView{

    private final AppController controller;

    @Inject
    public HomeView(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/", ctx -> ctx.render("index.jte"));
        app.get("/teacher/create", ctx -> ctx.render("createteacher.jte"));
        app.post("/teacher/create", this::handleCreateTeacher);
    }

    private void handleCreateTeacher(Context ctx) {
        String name = Objects.requireNonNull(ctx.formParam("name"));
        String surname = Objects.requireNonNull(ctx.formParam("surname"));

        TeacherDTO teacher = new TeacherDTO(0,name, surname);
        controller.getTeacherController().createTeacher(teacher);
        ctx.redirect("/");
    }

    @Override
    public String getViewName() {
        return "home";
    }


}