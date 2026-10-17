package fr.univ_amu.fr.formation.m1info.gla.api;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;


import static fr.univ_amu.fr.formation.m1info.gla.api.ApiUtils.*;

public class StudentAPI implements Api {
    private final AppController appController;

    @Inject
    public StudentAPI(AppController appController) {
        this.appController = appController;
    }

    /** Enregistre toutes les routes /api/students. */
    @Override
    public void registerRoutes(Javalin app) {

        // GET /api/students
        app.get("/api/students", this::handleGetAllStudents);

        // GET /api/students/{id}
        app.get("/api/students/{id}", this::handleGetStudentById);

        // DELETE /api/students/{id}
        app.delete("/api/students/{id}", this::handleDeleteStudentById);

        // POST /api/students
        app.post("/api/students", this::handleCreateStudent);

        // PUT /api/students/{id}
        app.put("/api/students/{id}", this::handleUpdateStudentById);
    }

    private void handleUpdateStudentById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        // 404 si l'étudiant n'existe pas
        try {
            appController.getStudentController().getStudentById(id);
        } catch (Exception e) {
            ctx.status(404);
            return;
        }

        StudentDTO body = readBody(ctx, StudentDTO.class);
        if (body == null) return;

        if (coursesExistinDb(body)) {
            ctx.status(422);
            return;
        }

        StudentDTO updated = new StudentDTO(
                id,
                body.name(),
                body.surname(),
                body.courses(),
                body.marks()
        );

        appController.getStudentController().updateStudent(updated);
        ctx.status(200);
        ctx.json(updated);
    }

    private void handleCreateStudent(Context ctx) {
        StudentDTO body = readBody(ctx, StudentDTO.class);
        if (body == null) return;

        if (coursesExistinDb(body)) {
            ctx.status(422);
            return;
        }

        StudentDTO created = appController.getStudentController().createStudent(body);
        ctx.status(201);
        ctx.json(created);
    }

    private void handleDeleteStudentById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        try {
            StudentDTO s = appController.getStudentController().getStudentById(id);
            appController.getStudentController().deleteStudent(s);
            ctx.status(204);
        } catch (Exception e) {
            ctx.status(404);
        }
    }

    private void handleGetStudentById(Context ctx) {
        Integer id = readIntPath(ctx, "id");
        if (id == null) return;

        try {
            ctx.json(appController.getStudentController().getStudentById(id));
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404);
        }
    }

    private void handleGetAllStudents(Context ctx) {
        ctx.json(appController.getStudentController().getAllStudents());
        ctx.status(200);
    }

    /* Vérifie que tous les course_id du StudentDTO existent côté serveur. */
    private boolean coursesExistinDb(StudentDTO dto) {
        if (dto.courses() == null) return false;

        for (Integer courseId : dto.courses()) {
            try {
                appController.getCoursController().getCourseById(courseId);
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }


}