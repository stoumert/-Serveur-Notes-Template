package fr.univ_amu.fr.formation.m1info.gla.storage.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
fournir une Connection JDBC à chaque appel.
Encapsule DriverManager.getConnection(...).
Permet de changer DB/config sans modifier les DAO.

 */
public final class JdbcConnectionProvider {
    private final AppConfig config;

    public JdbcConnectionProvider(AppConfig config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.dbUrl(),
                config.dbUsername(),
                config.dbPassword()
        );
    }
}
