@import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO
@import java.util.List

@param List<TeacherDTO> teachers


@template.layout(
title = "Créer un nouveau cours",
content = @`

    <div class="container">

        <div class="card">
            <h1>Créer un nouveau cours</h1>

            <form action="/courses/create" method="post" class="form-course">
                <div class="form-group">
                    <label for="name">Nom du cours</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4" required></textarea>
                </div>

                <div class="forme-group">
                    <label for="formule">Formule</label>
                    <input type="text" id="formule" name="formule" required>
                </div>

                <div class="form-group">
                    <label for="credits">Nombre de crédits</label>
                    <input type="number" id="credits" name="credits" required>
                </div>

                <div class="form-group">
                    <label for="teacherId">Enseignant</label>
                    <select id="teacherId" name="teacherId" required>
                        <option value="">-- Sélectionner un enseignant --</option>
                        @for(TeacherDTO teacher : teachers)
                            <option value="${teacher.id()}">
                                ${teacher.surname()} ${teacher.name()}
                            </option>
                        @endfor
                    </select>
                </div>

                <div class="actions">
                    <button type="submit" class="btn-primary">
                        Créer le cours
                    </button>

                    <a href="/courses" class="btn-secondary">
                        Annuler
                    </a>
                </div>

            </form>
        </div>

    </div>

`)
