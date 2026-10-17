package fr.univ_amu.fr.formation.m1info.gla.view;

import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.controller.AppController;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CourseView implements WebView{
    private final AppController controller;

    @Inject
    public CourseView(AppController controller) {
        this.controller = controller;
    }
    @Override
    public void registerRoutes(Javalin app) {
        app.get("/findcourse", ctx -> ctx.render("findcourse.jte"));
        app.get("/courses/create", this::handleRenderCreateCourse);

        app.post("/courses/create", this::handleCreateCourse);
        app.post("/course/find", this::handleFindCourse);
        app.get("/course/show", this::handleShowCourse);

        app.get("/course/grades", this::handleEditGrades);
        app.post("/course/grades/save", this::handleSaveGrades);
    }

    private void handleRenderCreateCourse(Context ctx) {
        List<TeacherDTO> teachers = controller.getTeacherController().getAllTeachers();
        Map<String, Object> model = new HashMap<>();
        model.put("teachers", teachers);
        ctx.render("createcourse.jte", model);
    }

    @Override
    public String getViewName() {
        return "courses";
    }

    private void handleFindCourse(Context ctx) {
        List<CourseDTO> listCourse = controller.getCoursController().getCoursesByName(ctx.formParam("name"));
        Map<String, Object> params = new HashMap<>();
        params.put("listCourses", listCourse);
        ctx.render("listcourses.jte", params);
    }

    private void handleShowCourse(Context ctx) {
        int id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        CourseDTO course = controller.getCoursController().getCourseById(id);
        List<StudentDTO> students = controller.getStudentController().findAllStudentsByCourse(id);
        Map<String, Object> params = new HashMap<>();
        params.put("course", course);
        params.put("students", students);
        ctx.render("fillmarks.jte", params);
    }

    private void handleEditGrades(Context ctx) {
        Integer studentId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("studentId")));
        int courseId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("courseId")));
        StudentDTO student = controller.getStudentController().getStudentById(studentId);
        CourseDTO course = controller.getCoursController().getCourseById(courseId);
        Map<String, Double> marks = student.marks().get(courseId);
        if (marks == null || marks.isEmpty()) {
            marks = new HashMap<>();
            for (String variable : controller.getCoursController().getVariableCourseFormula(course)){
                marks.put(variable, 0.0);
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("student", student);
        params.put("course", course);
        params.put("marks", marks);
        ctx.render("editgrades.jte", params);
    }

    private void handleSaveGrades(Context ctx) {
        Integer studentId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("studentId")));
        Integer courseId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("courseId")));
        Map<String, Double> newMarksForCourse = new HashMap<>();
        Map<String, List<String>> allFormParams = ctx.formParamMap();
        for (Map.Entry<String, List<String>> entry : allFormParams.entrySet()) {
            String paramName = entry.getKey();
            if (paramName.startsWith("marks[") && paramName.endsWith("]")) {
                String markName = paramName.substring(6, paramName.length() - 1);
                Double markValue = Double.parseDouble(entry.getValue().get(0));
                newMarksForCourse.put(markName, markValue);
            }
        }
        controller.getStudentController().updateStudentMarks(studentId, courseId, newMarksForCourse);
        ctx.redirect("/course/grades?studentId=" + studentId + "&courseId=" + courseId);
    }
    private void handleCreateCourse(Context ctx) {
        String name = Objects.requireNonNull(ctx.formParam("name"));
        String description = Objects.requireNonNull(ctx.formParam("description"));
        int teacherId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("teacherId")));
        String formula = Objects.requireNonNull(ctx.formParam("formule"));
        int credits = Integer.parseInt(Objects.requireNonNull(ctx.formParam("credits")));
        CourseDTO courseDTO = new CourseDTO(0, name, description,credits, formula, teacherId);
        controller.getCoursController().createCourse(courseDTO);
        ctx.redirect("/");
    }

}
