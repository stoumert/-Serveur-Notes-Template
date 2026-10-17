@import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO
@import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO

@param StudentDTO student
@param GradeDTO grades

@template.layout(title="Notes de " + student.surname() + " " + student.name(), content = @`
    @for(var course_marks : grades.grades().entrySet())
    <table>
        <tr>
            <td><b>Matière</b></td>
            @for(var markName : student.marks().get(course_marks.getKey().id()).keySet())
                <td><b>${markName}</b></td>
            @endfor
            <td><b>Note finale</b></td>
        </tr>

        <tr>
            <td>${course_marks.getKey().name()}</td>
            @for(var mark : student.marks().get(course_marks.getKey().id()).values())
                <td>${mark}</td>
            @endfor
            <td>${course_marks.getValue()}</td>
        </tr>
    </table>
        <br>
    @endfor
`)