@template.layout(title="Recherche d'enseignement", content = @`
    <form method="post" action="/course/find">
        <p>
            <label for="name"> Nom de l'enseignement :</label>
            <input type="text" name="name" id="name" required />
        </p>
        <p>
            <button>Rechercher</button>
        </p>
    </form>
`)