package fr.univ_amu.fr.formation.m1info.gla.view;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

public class StudentView implements WebView{
    private final AppController controller;
    @Inject
    public StudentView(AppController controller) {
        this.controller = controller;
    }
    @Override
    public void registerRoutes(Javalin app) {
        app.get("/findstudent", ctx -> ctx.render("findstudent.jte"));

        app.get("/student/create",ctx -> ctx.render("createstudent.jte"));
        app.post("/student/create",this::handleCreateStudent);

        app.get("/student/subscribe", this::handleRenderSubscribeStudents);
        app.post("/student/subscribe", this::handleSubscribeStudents);

        app.post("/student/find", this::handleFindStudent);
        app.get("/student/show", this::handleShowStudent);
    }

    private void handleRenderSubscribeStudents(Context ctx) {
        Integer id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));

        StudentDTO student = controller.getStudentController().getStudentById(id);
        List<Integer> currentCoursesSubscribed = student.courses();

        List<CourseDTO> courses = controller.getCoursController()
                .getAllCourses()
                .stream()
                .filter(courseDTO -> !currentCoursesSubscribed.contains(courseDTO.id()))
                .toList();

        Map<String, Object> model = new HashMap<>();
        model.put("student", student);
        model.put("courses", courses);

        ctx.render("subscribe.jte", model);
    }

    private void handleSubscribeStudents(Context ctx) {
        Integer id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        StudentDTO studentDTO = controller.getStudentController().getStudentById(id);
        List<String> selectedCourses = ctx.formParams("courseIds");
        List<Integer> coursesIds = new ArrayList<>();

        for (String courseIdStr : selectedCourses) {
            int courseId = Integer.parseInt(courseIdStr);
            coursesIds.add(courseId);
        }
        coursesIds.addAll(studentDTO.courses());

        controller.getStudentController().updateStudent(new StudentDTO(
                studentDTO.id(),
                studentDTO.name(),
                studentDTO.surname(),
                coursesIds,
                studentDTO.marks()
                )
        );
        ctx.redirect("/");
    }


    private void handleCreateStudent(Context ctx) {
        String name = Objects.requireNonNull(ctx.formParam("name"));
        String surname = Objects.requireNonNull(ctx.formParam("surname"));

        StudentDTO studentDTO = new StudentDTO(0,name, surname, List.of(), Map.of());
        StudentDTO student = controller.getStudentController().createStudent(studentDTO);
        ctx.redirect("/student/subscribe?id="+student.id());
    }
    @Override
    public String getViewName() {
        return "student";
    }
    private void handleFindStudent(Context ctx) {
        List<StudentDTO> listStudent = controller.getStudentController().findStudent(ctx.formParam("name"), ctx.formParam("surname"));
        Map<String, Object> params = new HashMap<>();
        params.put("listStudents", listStudent);
        ctx.render("liststudents.jte", params);
    }

    private void handleShowStudent(Context ctx) {
        Integer id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        StudentDTO student = controller.getStudentController().getStudentById(id);
        GradeDTO grades = controller.getStudentController().getStudentGradeDTO(student);
        Map<String, Object> params = new HashMap<>();
        params.put("grades", grades);
        params.put("student", student);
        ctx.render("grades.jte", params);
    }

}
