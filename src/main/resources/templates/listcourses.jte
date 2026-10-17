@import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO
@import java.util.List

@param List<CourseDTO> listCourses

@template.layout(title="Résultats de la recherche", content = @`
<ul>
    @for(var course : listCourses)
        <li><a href="/course/show?id=${course.id()}">${course.name()} </a></li>
    @endfor
</ul>
`)