@template.layout(title="Crée un professeur", content = @`
    <h1>Crée un professeur</h1>

    <div class="snippet-container">
        <div class="card">
            <form action="/teacher/create" method="post" class="form-course">
                <div class="forme-group">
                    <label for="name">Nom du professeur</label>
                    <input name="name" id="name" type="text" required>
                </div>
                <div class="forme-group">
                    <label for="surname">Prénom du professeur</label>
                    <input name="surname" id="surname" type="text" required>
                </div>
                <div class="actions">
                    <button type="submit" class="btn-primary">
                        Créer le professeur
                    </button>

                    <a href="/" class="btn-secondary">
                        Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
`)
