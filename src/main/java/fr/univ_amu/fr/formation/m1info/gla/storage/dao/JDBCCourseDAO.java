package fr.univ_amu.fr.formation.m1info.gla.storage.dao;

import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DBUtils;
import fr.univ_amu.fr.formation.m1info.gla.storage.jdbc.JdbcConnectionProvider;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCCourseDAO implements DAO<Integer, CourseDTO> {
    private final JdbcConnectionProvider connectionProvider = DBUtils.getConnectionProvider();

    @Override
    public Optional<CourseDTO> get(Integer key) {
        String sql = """
                SELECT c.id, c.name, c.description, c.credits, c.supervisor_id, cs.formula
                FROM courses c
                LEFT JOIN course_session cs ON c.id = cs.course_id
                WHERE c.id = ?
                """;
        try (Connection conn = connectionProvider.getConnection()){
            //conn.setTransactionIsolation();
            try (PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1, key);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        CourseDTO courseDTO = new CourseDTO(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getInt("credits"),
                                rs.getString("formula"),
                                rs.getInt("supervisor_id")
                        );
                        return Optional.of(courseDTO);
                    }
                }
            }catch (SQLException e){
                conn.rollback();
            }finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<CourseDTO> getAll() {
        String sql = """
                SELECT c.id, c.name, c.description, c.credits, c.supervisor_id, cs.formula
                FROM courses c LEFT JOIN course_session cs ON c.id = cs.course_id
               
                """;
        List<CourseDTO> courseDTOs = new ArrayList<>();
        try (
                Connection conn = connectionProvider.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CourseDTO courseDTO = new CourseDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits"),
                        rs.getString("formula"),
                        rs.getInt("supervisor_id")
                );
                courseDTOs.add(courseDTO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseDTOs;
    }

    @Override
    public Integer create(CourseDTO element) {
        int generatedId = 0;
        try(Connection conn = connectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO courses (name, description, credits, supervisor_id) VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            )) {
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);

            stmt.setString(1, element.name());
            stmt.setString(2, element.description());
            stmt.setInt(3, element.credits());
            stmt.setInt(4, element.supervisorId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
            insertCourseInCourseSession(conn, generatedId, Year.now().getValue(), element.formula());
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du cours: " + e.getMessage());
        }
        return generatedId;
    }

    @Override
    public void update(CourseDTO element) {
        try (Connection conn = connectionProvider.getConnection()){
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            String sqlCourse = "UPDATE courses SET name = ?, description = ?, credits = ?, supervisor_id = ? WHERE id = ?";
            PreparedStatement stmtCourse = conn.prepareStatement(sqlCourse);
            stmtCourse.setString(1, element.name());
            stmtCourse.setString(2, element.description());
            stmtCourse.setInt(3, element.credits());
            stmtCourse.setInt(4, element.supervisorId());
            stmtCourse.setInt(5, element.id());
            stmtCourse.executeUpdate();
            stmtCourse.close();

            String sqlSession = "UPDATE course_session SET formula = ? WHERE course_id = ? AND YEAR = ?";
            PreparedStatement stmtSession = conn.prepareStatement(sqlSession);
            stmtSession.setString(1, element.formula());
            stmtSession.setInt(2, element.id());
            stmtSession.setInt(3, Year.now().getValue());
            stmtSession.executeUpdate();
            stmtSession.close();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(CourseDTO element) {
        try (Connection conn = connectionProvider.getConnection()){
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            String sqlSession = "DELETE FROM course_session WHERE course_id = ?";
            PreparedStatement stmtSession = conn.prepareStatement(sqlSession);
            stmtSession.setInt(1, element.id());
            stmtSession.executeUpdate();
            stmtSession.close();

            String sqlCourse = "DELETE FROM courses WHERE id = ?";
            PreparedStatement stmtCourse = conn.prepareStatement(sqlCourse);
            stmtCourse.setInt(1, element.id());
            stmtCourse.executeUpdate();
            stmtCourse.close();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void insertCourseInCourseSession(Connection conn, int coursId, int year, String formula) {
        String sql = "INSERT INTO course_session (course_id, year, formula) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coursId);
            stmt.setInt(2, year);
            stmt.setString(3, formula);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}