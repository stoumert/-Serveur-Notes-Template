@template.layout(title="Recherche d'étudiant", content = @`
    <form method="post" action="/student/find">
        <p>
            <label for="name">Nom de l'étudiant :</label>
            <input type="text" name="name" id="name" required />
        </p>
        <p>
            <label for="surname">Prénom de l'étudiant :</label>
            <input type="text" name="surname" id="surname" required />
        </p>
        <p>
            <button>Rechercher</button>
        </p>
    </form>
`)