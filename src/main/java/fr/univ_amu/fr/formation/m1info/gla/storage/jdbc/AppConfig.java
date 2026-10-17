package fr.univ_amu.fr.formation.m1info.gla.storage.jdbc;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/*
charger configuration.properties depuis le classpath et exposer des getters.
Lit le fichier dans src/main/resources.
Lève une erreur claire si le fichier n’existe pas ou est illisible.
 */

public final class AppConfig {
    private final Properties props = new Properties();

    public AppConfig() {
        try (InputStream in = AppConfig.class.getClassLoader()
                .getResourceAsStream("configuration.properties")) {
            if (in == null) {
                throw new IllegalStateException("configuration.properties not found in resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load configuration.properties", e);
        }
    }

    public String dbUrl() { return props.getProperty("db_url"); }
    public String dbUsername() { return props.getProperty("db_username"); }
    public String dbPassword() { return props.getProperty("db_password"); }
    public String daoType() { return props.getProperty("dao_type"); }
}

