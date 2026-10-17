@import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO
@import java.util.List

@param List<StudentDTO> listStudents

@template.layout(title="Résultats de la recherche", content = @`
    <ul>
        @for(var student : listStudents)
            <li>
                ${student.name()} ${student.surname()}
                |
                <a href="/student/show?id=${student.id()}">Voir</a>
                |
                <a href="/student/subscribe?id=${student.id()}">Inscrire</a>
            </li>
        @endfor
    </ul>

`)