package fr.univ_amu.fr.formation.m1info.gla.api;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;


import static fr.univ_amu.fr.formation.m1info.gla.api.ApiUtils.*;

public class CourseAPI implements Api {

    private final AppController appController;

    @Inject
    public CourseAPI(AppController appController) {
        this.appController = appController;
    }

    /** Enregistre toutes les routes /api/courses. */
    @Override
    public void registerRoutes(Javalin app) {

        // GET /api/courses
        app.get("/api/courses", this::handleGetAllCourses);

        // GET /api/courses/{id}
        app.get("/api/courses/{id}", this::handleGetCourseById);

        // DELETE /api/courses/{id}
        app.delete("/api/courses/{id}", this::handleDeleteCourseById);

        // POST /api/courses
        app.post("/api/courses", this::handleCreateCourses);

        // PUT /api/courses/{id}
        app.put("/api/courses/{id}", this::handleUpdateCourseById);
    }

    private void handleUpdateCourseById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        // 404 si le cours n'existe pas
        try {
            appController.getCoursController().getCourseById(id);
        } catch (Exception e) {
            ctx.status(404);
            return;
        }

        CourseDTO body = readBody(ctx, CourseDTO.class);
        if (checkBodyOfRequest(ctx, body)) return;

        CourseDTO updated = new CourseDTO(
                id,
                body.name(),
                body.description(),
                body.credits(),
                body.formula(),
                body.supervisorId()
        );

        appController.getCoursController().updateCourse(updated);
        ctx.status(200);
        ctx.json(updated);
    }

    private boolean checkBodyOfRequest(Context ctx, CourseDTO body) {
        if (body == null) return true;

        if (appController.getTeacherController().getTeacherById(body.supervisorId()) == null) {
            ctx.status(422);
            return true;
        }

        return false;
    }

    private void handleCreateCourses(Context ctx) {
        CourseDTO body = readBody(ctx, CourseDTO.class);
        if (checkBodyOfRequest(ctx, body)) return;

        CourseDTO created = appController.getCoursController().createCourse(body);
        ctx.status(201);
        ctx.json(created);
    }

    private void handleDeleteCourseById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        try {
            CourseDTO c = appController.getCoursController().getCourseById(id);
            appController.getCoursController().deleteCourse(c);
            ctx.status(204);
        } catch (Exception e) {
            ctx.status(404);
        }
    }

    private void handleGetCourseById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        try {
            ctx.json(appController.getCoursController().getCourseById(id));
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404);
        }
    }

    private void handleGetAllCourses(Context ctx) {
        ctx.json(appController.getCoursController().getAllCourses());
        ctx.status(200);
    }


}
