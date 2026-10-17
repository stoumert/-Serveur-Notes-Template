@template.layout(title="Crée un étudiant", content = @`
    <h1>Crée un étudiant</h1>

    <div class="snippet-container">
        <div class="card">
            <form action="/student/create" method="post" class="form-course">
                <div class="forme-group">
                    <label for="name">Nom de l'étudiant</label>
                    <input name="name" id="name" type="text" required>
                </div>
                <div class="forme-group">
                    <label for="surname">Prénom de l'étudiant</label>
                    <input name="surname" id="surname" type="text" required>
                </div>
                <div class="actions">
                    <button type="submit" class="btn-primary">
                        Créer l'étudiant
                    </button>

                    <a href="/" class="btn-secondary">
                        Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
`)
