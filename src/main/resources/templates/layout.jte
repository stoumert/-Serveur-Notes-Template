@import gg.jte.Content
@param String title
@param Content content

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        nav {
            background-color: #333;
            overflow: hidden;
            width: 100%;
            height: 50px;
            display: flex;
            align-items: center;
            padding: 0 15px;
            box-sizing: border-box;
        }

        nav a {
            display: flex;
            align-items: center;
            text-decoration: none;
        }

        nav a img {
            height: 30px;
            width: 30px;
            filter: invert(100%);
            cursor: pointer;
        }

        .main-container {
            padding: 20px;
        }
    </style>
</head>
<body>

<nav>
    <a href="/">
        <img src="/images/home.svg" alt="Accueil">
    </a>
</nav>

<div class="main-container">
    ${content}
</div>

</body>
</html>