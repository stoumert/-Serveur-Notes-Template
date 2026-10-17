@import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO
@import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO
@import java.util.Map

@param StudentDTO student
@param CourseDTO course
@param Map<String, Double> marks

@template.layout(
title = "Éditer les notes - " + student.surname() + " " + student.name(),
content = @`

    <div class="container">

        <div class="card">
            <h1>Édition des notes</h1>

            <div class="info">
                <p><strong>Étudiant :</strong> ${student.surname()} ${student.name()}</p>
                <p><strong>Cours :</strong> ${course.name()}</p>
            </div>

            @if(marks != null && !marks.isEmpty())

                <form action="/course/grades/save" method="post" class="form-grades">

                    <input type="hidden" name="studentId" value="${student.id()}">
                    <input type="hidden" name="courseId" value="${course.id()}">

                    <table>
                        <thead>
                        <tr>
                            <th>Évaluation</th>
                            <th>Note</th>
                        </tr>
                        </thead>
                        <tbody>
                        @for(var entry : marks.entrySet())
                            <tr>
                                <td>
                                    <label for="mark-${entry.getKey()}">
                                        ${entry.getKey()}
                                    </label>
                                </td>
                                <td>
                                    <input
                                            type="number"
                                            id="mark-${entry.getKey()}"
                                            name="marks[${entry.getKey()}]"
                                            value="${entry.getValue()}"
                                            step="0.1"
                                            min="0"
                                            max="20"
                                            required
                                    >
                                </td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>

                    <div class="actions">
                        <button type="submit" class="btn-primary">
                            Enregistrer les modifications
                        </button>

                        <a href="/course/show?id=${course.id()}" class="btn-secondary">
                            Annuler
                        </a>
                    </div>

                </form>

            @else
                <p class="empty-message">Aucune note enregistrée pour ce cours.</p>
            @endif

        </div>

    </div>

`)
