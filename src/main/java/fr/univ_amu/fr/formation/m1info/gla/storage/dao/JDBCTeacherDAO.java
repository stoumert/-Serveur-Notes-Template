package fr.univ_amu.fr.formation.m1info.gla.storage.dao;

import fr.univ_amu.fr.formation.m1info.gla.data.TeacherDTO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.DBUtils;
import fr.univ_amu.fr.formation.m1info.gla.storage.jdbc.JdbcConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCTeacherDAO implements DAO<Integer, TeacherDTO> {
    private final JdbcConnectionProvider connectionProvider = DBUtils.getConnectionProvider();
    @Override
    public Optional<TeacherDTO> get(Integer key) {
        try (Connection conn = connectionProvider.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM teachers WHERE id = ?");
            try (ResultSet rs = stmt.executeQuery()) {
                stmt.setInt(1, key);
                if (rs.next()) {
                    TeacherDTO teacherDTO = new TeacherDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname")
                    );
                    return Optional.of(teacherDTO);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<TeacherDTO> getAll() {
        List<TeacherDTO> teacherDTOs = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection()){
            String sql = "SELECT id, name, surname FROM teachers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            stmt.close();
            while (rs.next()) {
                TeacherDTO teacherDTO = new TeacherDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname")
                );
                teacherDTOs.add(teacherDTO);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teacherDTOs;
    }

    @Override
    public Integer create(TeacherDTO element) {
        int id = -1;
        try (Connection conn = connectionProvider.getConnection()){
            String sql = "INSERT INTO teachers (name, surname) VALUES (?, ?)";
            try(PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)){
                stmt.setString(1, element.name());
                stmt.setString(2, element.surname());
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (!keys.next()) throw new IllegalStateException("Aucune clé générée pour students");
                    return keys.getInt(1);
                }
            }catch (SQLException e) {
                conn.rollback();
            }finally {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public void update(TeacherDTO element) {
        try (Connection conn = connectionProvider.getConnection()){
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            String sql = "UPDATE teachers SET name = ?, surname = ? WHERE id = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, element.name());
                stmt.setString(2, element.surname());
                stmt.setInt(3, element.id());
                stmt.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
                conn.rollback();
            }finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(TeacherDTO element) {
        try(Connection conn = connectionProvider.getConnection()) {
            String sql = "DELETE FROM teachers WHERE id = ?";
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1,element.id());
                stmt.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
                conn.rollback();
            }finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
