@import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO
@import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO
@import java.util.List

@param CourseDTO course
@param List<StudentDTO> students

@template.layout(title="Saisie des notes : " + course.name(), content = @`
<form action="/course/grades" method="get">
    <label for="student">Choisir un étudiant :</label>
    <select name="studentId" id="student">
        @for(StudentDTO student : students)
            <option value="${student.id()}">${student.surname()} ${student.name()}</option>
        @endfor
    </select>
    <input type="hidden" name="courseId" value="${course.id()}">
    <button type="submit">Afficher les notes</button>
</form>
`)