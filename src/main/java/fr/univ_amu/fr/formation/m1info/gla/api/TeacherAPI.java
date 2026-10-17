package fr.univ_amu.fr.formation.m1info.gla.api;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static fr.univ_amu.fr.formation.m1info.gla.api.ApiUtils.*;

public class TeacherAPI implements Api {

    private final AppController appController;

    @Inject
    public TeacherAPI(AppController appController) {
        this.appController = appController;
    }

    // Enregistre toutes les routes /api/teachers
    @Override
    public void registerRoutes(Javalin app) {

        // GET /api/teachers
        app.get("/api/teachers", this::handleGetAllTeachers);

        // GET /api/teachers/{id}
        app.get("/api/teachers/{id}", this::handleFindTeacherById);

        // DELETE /api/teachers/{id}
        app.delete("/api/teachers/{id}", this::handleDeleteTeacherById);

        // POST /api/teachers
        app.post("/api/teachers", this::handleCreateTeacher);

        // PUT /api/teachers/{id}
        app.put("/api/teachers/{id}", this::handleUpdateTeacherById);
    }

    private void handleGetAllTeachers(Context ctx) {
        ctx.json(appController.getTeacherController().getAllTeachers());
        ctx.status(200);
    }

    private void handleUpdateTeacherById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        //404 si l'enseignant n'existe pas
        TeacherDTO existing = appController.getTeacherController().getTeacherById(id);
        if (existing == null) {
            ctx.status(404);
            return;
        }

        TeacherDTO body = readBody(ctx, TeacherDTO.class);
        if (checkBodyOfRequest(body)){
            ctx.status(400);
            return;
        }

        TeacherDTO updated = new TeacherDTO(id, body.name(), body.surname());
        appController.getTeacherController().updateTeacher(updated);

        ctx.status(200);
        ctx.json(updated);
    }

    private void handleCreateTeacher(Context ctx) {
        TeacherDTO body = readBody(ctx, TeacherDTO.class);
        if (checkBodyOfRequest(body)){
            ctx.status(400);
            return;
        }

        TeacherDTO created = appController.getTeacherController().createTeacher(
                new TeacherDTO(null, body.name(), body.surname())
        );

        ctx.status(201);
        ctx.json(created);
    }

    private void handleDeleteTeacherById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        TeacherDTO t = appController.getTeacherController().getTeacherById(id);
        if (t == null) {
            ctx.status(404);
            return;
        }

        appController.getTeacherController().deleteTeacher(t);
        ctx.status(204);
    }

    private void handleFindTeacherById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        TeacherDTO t = appController.getTeacherController().getTeacherById(id);
        if (t == null) {
            ctx.status(404);
            return;
        }

        ctx.json(t);
        ctx.status(200);
    }
    private boolean checkBodyOfRequest(TeacherDTO body) {
        if (body == null) return true;
        return body.name() == null || body.name().trim().isEmpty()
                || body.surname() == null || body.surname().trim().isEmpty();
    }
}
