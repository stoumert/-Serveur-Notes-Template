@import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO
@import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO
@import java.util.List

@param StudentDTO student
@param List<CourseDTO> courses 

@template.layout(title="Inscription aux cours", content = @`
    <h1>Inscription aux cours</h1>
    
    <p>Etudiant : ${student.surname()} ${student.name()}</p>

    <form method="post" action="/student/subscribe?id=${student.id()}">
        <input type="hidden" name="studentId" value="${student.id()}">
    
        <ul>
            @for(CourseDTO course : courses)
                <li>
                    <label>
                        <input type="checkbox" name="courseIds" value="${course.id()}">
                        ${course.name()}
                    </label>
                </li>
            @endfor
        </ul>
    
        <button type="submit">Valider l'inscription</button>
    </form>
`)