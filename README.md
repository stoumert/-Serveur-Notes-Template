# Serveur monolithique de saisie des notes

## Présentation

Ce projet est une application web monolithique développée en **Java** permettant la gestion complète :

- des enseignants
- des étudiants
- des cours
- des inscriptions
- des notes
- des formules de calcul des moyennes

---
# Commandes

## Lancer le projet
```bash
    ./gradlew run
```
## Lancer les tests
```bash
    ./gradlew test 
```

## Lancer couverture de test
```bash
    ./gradlew jacocoTestReport
```

# Architecture Générale

L’organisation suit le schéma suivant :



## Dépendences

- [Javalin](https://javalin.io) pour la gestion du serveur web.
- [Jte](https://jte.gg/) pour la construction des templates.
- [slf4j](https://slf4j.org/) pour les logs.
- [antlr4](https://www.antlr.org/) pour l'analyse lexicale et syntaxique.
- [JUnit](https://junit.org/junit5/) pour les tests unitaires.
- [assertJ](https://assertj.github.io/doc/) pour les assertions dans les tests unitaires.

---

#  Description des modules

## api/

Expose une API applicative :

- `Api.java`
- `ConcreteApi.java`
- `StudentAPI.java`
- `TeacherAPI.java`
- `CourseAPI.java`
- `ApiUtils.java`

---

##  configuration/

Configuration de l'application :

- `AppConfig.java`
- `GuiceConfig.java`
- `Http.java`

Injection de dépendances via **Guice**.

---

##  controller/

Couche d’orchestration entre View/API et Services :

- `AppController`
- `CourseController`
- `StudentController`
- `TeacherController`
- Versions concrètes correspondantes

---

## service/

Contient la logique métier :

- `CourseService`
- `StudentService`
- `TeacherService`

Rôle :
- Orchestration des opérations
- Application des règles métier
- Interaction avec les repositories

---

## repository/

Gestion de la persistance logique :

- `CourseRepository`
- `StudentRepository`
- `TeacherRepository`

Responsabilités :
- Synchronisation avec les managers
- Conversion Model ↔ DTO
- Interaction avec les DAO

---

## model/

Contient les entités métier :

- `Course`
- `Student`
- `Teacher`
- `CourseManager`
- `StudentManager`
- `TeacherManager`
- `Manager`

Gestion des identifiants et règles métier internes.

---

## data/

Contient :

- `DTO`
- `Convertor`
- `Exceptions`

### DTO principaux :

- `CourseDTO`
- `StudentDTO`
- `TeacherDTO`
- `GradeDTO`

### Convertors :

- `CourseConvertor`
- `StudentConvertor`
- `TeacherConvertor`
- `GradeConvertor`

---

## data/formulas/

Gestion des formules de calcul :

- `CourseFormulaDTO`
- `CourseFormulaConvertor`
- `FormulaNode`
- `FormulaNodeVisitor`
- `SumNode`
- `MaxNode`
- `VariableNode`
- `WeightNode`
- Visitors DTO ↔ Formula

---

##  model/formulas/

Implémentation métier des formules :

- `Formula`
- `CourseFormula`
- `SumFormula`
- `MaxFormula`
- `WeightedFormula`
- `VariableFormula`
- `FormulaParser`
- `FormulaFactory`
- `FormulaVisitor`
- `ToStringFormulaVisitor`

---

## technical/

Couche technique dédiée à ANTLR :

- `AntlrFormulaParser`
- `AntlrFormulaVisitor`
- `FormulaParserErrorListener`
- `FormulaParserException`

---

##  storage/

Gestion du stockage :

### DAO JDBC

- `JDBCCourseDAO`
- `JDBCStudentDAO`
- `JDBCTeacherDAO`

### Infrastructure

- `JdbcConnectionProvider`
- `DBUtils`
- `DAO`
- `InMemoryDAO`
- `IdentifierFactory`
- `IntegerFactory`

### XML

- `FormulaCourseDTOXMLConvertor`

---

##  view/

Couche présentation utilisant :

- **Javalin** pour le serveur web
- **JTE** pour les templates HTML

Classes principales :

- `CourseView`
- `StudentView`
- `HomeView`
- `ConcreteWebView`
- `WebView`

---

# Fonctionnalités

- Création / modification / suppression d’enseignants
- Création / modification / suppression de cours
- Inscription des étudiants aux cours
- Saisie et modification des notes
- Gestion de formules dynamiques via ANTLR
- Calcul automatique des moyennes
- Consultation des résultats

---

# Conversion Model ↔ DTO

Les convertisseurs assurent la transformation entre :

- entités métier (`model`)
- objets de transfert (`data`)

Cela permet :

- une séparation stricte des couches
- une indépendance vis-à-vis du stockage
- une meilleure maintenabilité

---

# Gestion des formules

Le projet intègre :

- Un parser ANTLR
- Un système de Visitor Pattern
- Des formules composées (Sum, Max, Weighted, Variable)

Les formules sont :

1. Parsées
2. Transformées en arbre syntaxique
3. Converties en objets métier
4. Évaluées dynamiquement

---

#  Tests
Utilisation des Mock pour les tests
Frameworks utilisés :

- JUnit 5
- AssertJ

Les tests couvrent :

- La logique métier
- L’évaluation des formules
- Les services


