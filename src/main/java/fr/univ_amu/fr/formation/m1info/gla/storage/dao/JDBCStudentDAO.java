package fr.univ_amu.fr.formation.m1info.gla.storage.dao;

import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DBUtils;
import fr.univ_amu.fr.formation.m1info.gla.storage.jdbc.JdbcConnectionProvider;

import java.sql.*;
import java.util.*;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;


public final class JDBCStudentDAO implements DAO<Integer, StudentDTO> {

    private final JdbcConnectionProvider connectionProvider = DBUtils.getConnectionProvider();

    @Override
    public Optional<StudentDTO> get(Integer key) {
        try (Connection con = connectionProvider.getConnection()) {
            return getWithConnection(con, key);
        } catch (SQLException e) {
            throw new RuntimeException("JDBCStudentDAO.get failed id=" + key, e);
        }
    }

    @Override
    public List<StudentDTO> getAll() {
        final String sql = "SELECT id FROM students ORDER BY id";
        List<StudentDTO> out = new ArrayList<>();

        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idStudent = rs.getInt("id");
                out.add(getWithConnection(con, idStudent).orElseThrow());
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("JDBCStudentDAO.getAll failed", e);
        }
    }


    @Override
    public Integer create(StudentDTO element) {
        try (Connection con = connectionProvider.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            try {
                int studentId = insertStudent(con, element);

                List<Integer> courseIds = normalizeCourseIds(element.courses());
                validateMarksAgainstCourses(element.marks(), courseIds);

                insertEnrollments(con, studentId, courseIds);
                insertGrades(con, studentId, element.marks(), courseIds);

                con.commit();
                return studentId;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("JDBCStudentDAO.create failed", e);
        }
    }

    @Override
    public void update(StudentDTO element) {
        try (Connection con = connectionProvider.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            try {
                requireStudentExists(con, element.id());

                updateStudentBase(con, element);

                deleteGradesForStudent(con, element.id());
                deleteEnrollmentsForStudent(con, element.id());

                List<Integer> courseIds = normalizeCourseIds(element.courses());
                validateMarksAgainstCourses(element.marks(), courseIds);

                insertEnrollments(con, element.id(), courseIds);
                insertGrades(con, element.id(), element.marks(), courseIds);

                con.commit();

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("JDBCStudentDAO.update failed id=" + element.id(), e);
        }
    }


    @Override
    public void delete(StudentDTO element) {
        try (Connection con = connectionProvider.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            try {
                requireStudentExists(con, element.id());

                deleteGradesForStudent(con, element.id());
                deleteEnrollmentsForStudent(con, element.id());

                try (PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id=?")) {
                    ps.setInt(1, element.id());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("JDBCStudentDAO.delete failed id=" + element.id(), e);
        }
    }

    /**
     * Version interne de get() qui réutilise la même connexion.
     */
    private Optional<StudentDTO> getWithConnection(Connection con, int studentId) throws SQLException {
        Optional<StudentDTO> base = loadStudentBase(con, studentId);
        if (base.isEmpty()) return Optional.empty();

        List<Integer> courses = loadCoursesFromEnrollment(con, studentId);
        Map<Integer, Map<String, Double>> marks = loadMarksFromGrades(con, studentId);

        StudentDTO b = base.get();
        return Optional.of(new StudentDTO(b.id(), b.name(), b.surname(), courses, marks));
    }

    /**
     * Charge uniquement (id, name, surname) depuis students.
     */
    private Optional<StudentDTO> loadStudentBase(Connection con, int id) throws SQLException {
        final String sql = "SELECT id, name, surname FROM students WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new StudentDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        List.of(),
                        Map.of()
                ));
            }
        }
    }

    /**
     * Retourne les course_id d'un étudiant via enrollment -> course_session.
     */
    private List<Integer> loadCoursesFromEnrollment(Connection con, int studentId) throws SQLException {
        final String sql = """
            SELECT DISTINCT cs.course_id
            FROM enrollment e
            JOIN course_session cs ON cs.id = e.session_id
            WHERE e.student_id = ?
            ORDER BY cs.course_id
            """;
        List<Integer> out = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getInt("course_id"));
            }
        }
        return out;
    }

    /**
     * Retourne les notes groupées par course_id via grades -> course_session.
     */
    private Map<Integer, Map<String, Double>> loadMarksFromGrades(Connection con, int studentId) throws SQLException {
        final String sql = """
            SELECT cs.course_id, g.grade_type, g.grade
            FROM grades g
            JOIN course_session cs ON cs.id = g.session_id
            WHERE g.student_id = ?
            """;
        Map<Integer, Map<String, Double>> out = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int courseId = rs.getInt("course_id");
                    String type = rs.getString("grade_type");
                    double grade = rs.getDouble("grade");
                    out.computeIfAbsent(courseId, k -> new HashMap<>()).put(type, grade);
                }
            }
        }
        return out;
    }


    /**
     * Insère un étudiant dans students et retourne l'id généré.
     */
    private int insertStudent(Connection con, StudentDTO element) throws SQLException {
        final String sql = "INSERT INTO students(name, surname) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, element.name());
            ps.setString(2, element.surname());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new IllegalStateException("Aucune clé générée pour students");
                return keys.getInt(1);
            }
        }
    }

    /**
     * Met à jour les champs de base de students.
     */
    private void updateStudentBase(Connection con, StudentDTO element) throws SQLException {
        final String sql = "UPDATE students SET name=?, surname=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, element.name());
            ps.setString(2, element.surname());
            ps.setInt(3, element.id());
            ps.executeUpdate();
        }
    }

    /**
     * Insère enrollment (student_id, session_id) à partir d'une liste de course_id.
     * Pour chaque course_id, on prend la session la plus récente (max year).
     */
    private void insertEnrollments(Connection con, int studentId, List<Integer> courseIds) throws SQLException {
        if (courseIds == null || courseIds.isEmpty()) return;

        final String sql = "INSERT INTO enrollment(student_id, session_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Integer courseId : courseIds) {
                requireCourseExists(con, courseId);

                Integer sessionId = findLatestSessionId(con, courseId);
                if (sessionId == null) throw new IllegalStateException("Aucune session pour course_id=" + courseId);

                ps.setInt(1, studentId);
                ps.setInt(2, sessionId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertGrades(Connection con,
                              int studentId,
                              Map<Integer, Map<String, Double>> marks,
                              List<Integer> courseIds) throws SQLException {
        if (marks == null || marks.isEmpty()) return;

        requireStudentExists(con, studentId);

        final String sql = "INSERT INTO grades(student_id, session_id, grade_type, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (var entry : marks.entrySet()) {
                int courseId = entry.getKey();

                // cohérence : marks doit être un sous-ensemble de courses
                if (courseIds != null && !courseIds.contains(courseId)) {
                    throw new IllegalArgumentException("marks contient course_id=" + courseId + " mais courses ne le contient pas");
                }

                requireCourseExists(con, courseId);

                Integer sessionId = findLatestSessionId(con, courseId);
                if (sessionId == null) throw new IllegalStateException("Aucune session pour course_id=" + courseId);

                Map<String, Double> exams = entry.getValue();
                if (exams == null || exams.isEmpty()) continue;

                for (var examEntry : exams.entrySet()) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, sessionId);
                    ps.setString(3, examEntry.getKey());
                    ps.setDouble(4, examEntry.getValue());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    /**
     * Supprime toutes les inscriptions d'un étudiant.
     */
    private void deleteEnrollmentsForStudent(Connection con, int studentId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM enrollment WHERE student_id=?")) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    /**
     * Supprime toutes les notes d'un étudiant.
     */
    private void deleteGradesForStudent(Connection con, int studentId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM grades WHERE student_id=?")) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    /**
     * Vérifie que l'étudiant existe.
     */
    private void requireStudentExists(Connection con, int studentId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM students WHERE id=?")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("Student inexistant id=" + studentId);
            }
        }
    }

    /**
     * Vérifie que le cours existe.
     */
    private void requireCourseExists(Connection con, int courseId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM courses WHERE id=?")) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("Course inexistant id=" + courseId);
            }
        }
    }

    /**
     * Retourne l'id de la session la plus récente d'un cours (max year).
     */
    private Integer findLatestSessionId(Connection con, int courseId) throws SQLException {
        final String sql = """
            SELECT id
            FROM course_session
            WHERE course_id=?
            ORDER BY year DESC
            FETCH FIRST 1 ROW ONLY
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("id");
            }
        }
    }

    /**
     * Dé-duplique et normalise une liste de course_id pour éviter des insertions en double
     * (sinon violation de PK dans enrollment).
     */
    private List<Integer> normalizeCourseIds(List<Integer> courseIds) {
        if (courseIds == null) return List.of();
        LinkedHashSet<Integer> set = new LinkedHashSet<>(courseIds);
        return new ArrayList<>(set);
    }

    /**
     * Vérifie que marks ne contient pas de course_id hors de la liste courses.
     */
    private void validateMarksAgainstCourses(Map<Integer, Map<String, Double>> marks, List<Integer> courseIds) {
        if (marks == null || marks.isEmpty()) return;
        if (courseIds == null) courseIds = List.of();

        for (Integer courseId : marks.keySet()) {
            if (!courseIds.contains(courseId)) {
                throw new IllegalArgumentException("marks contient course_id=" + courseId + " mais courses ne le contient pas");
            }
        }
    }
}
