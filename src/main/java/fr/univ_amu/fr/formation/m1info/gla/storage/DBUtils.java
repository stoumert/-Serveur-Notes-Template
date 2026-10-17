package fr.univ_amu.fr.formation.m1info.gla.storage;

import fr.univ_amu.fr.formation.m1info.gla.storage.jdbc.AppConfig;
import fr.univ_amu.fr.formation.m1info.gla.storage.jdbc.JdbcConnectionProvider;

import java.sql.*;

public class DBUtils {
    public void initializeDB(){
        JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider(new AppConfig());
        try(Connection conn = connectionProvider.getConnection()) {
            createTableTeacher(conn);
            createTableStudent(conn);
            createTableCourse(conn);
            createTableCourseSession(conn);
            createTableGrade(conn);
            createTableEnrollment(conn);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation des tables "+ e.getMessage());
        }
    }
    public static JdbcConnectionProvider getConnectionProvider() {
        return new JdbcConnectionProvider(new AppConfig());
    }
    private void createTableTeacher(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS teachers (" +
                "id INTEGER IDENTITY PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "surname VARCHAR(255) NOT NULL );";
        try(Statement stmt = conn.createStatement()
        ){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table teacher "+ e.getMessage());
        }

    }
    private void createTableCourse(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS courses ("+
                "id INTEGER IDENTITY PRIMARY KEY,"+
                "name VARCHAR(255) NOT NULL,"+
                "description VARCHAR(255) NOT NULL,"+
                "credits INTEGER NOT NULL,"+
                "supervisor_id INTEGER NOT NULL,"+
                "FOREIGN KEY (supervisor_id) REFERENCES teachers (id));";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table course"+ e.getMessage());
        }
    }

    private void createTableStudent(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS students ("+
                "id INTEGER IDENTITY PRIMARY KEY,"+
                "name VARCHAR(25) NOT NULL,"+
                "surname VARCHAR(25) NOT NULL);";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table student"+ e.getMessage());
        }
    }
    private void createTableGrade(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS grades (" +
                "student_id INTEGER NOT NULL," +
                "session_id INTEGER NOT NULL," +
                "grade_type VARCHAR(25) NOT NULL," +
                "grade DOUBLE NOT NULL," +
                "PRIMARY KEY (student_id,session_id,grade_type)," +
                "FOREIGN KEY (student_id) REFERENCES students (id)," +
                "FOREIGN KEY (session_id) REFERENCES course_session (id));";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table grade "+ e.getMessage());
        }
    }
    private void createTableCourseSession(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS course_session ("+
                        "id INTEGER IDENTITY PRIMARY KEY,"+
                        "course_id INTEGER NOT NULL,"+
                        "year INTEGER NOT NULL,"+
                        "formula VARCHAR(255) NOT NULL,"+
                        "FOREIGN KEY (course_id) REFERENCES courses (id));";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table course_session"+ e.getMessage());
        }

    }
    private void createTableEnrollment(Connection conn){
        String query = "CREATE TABLE IF NOT EXISTS enrollment ("+
                "student_id INTEGER NOT NULL,"+
                "session_id INTEGER NOT NULL,"+
                "PRIMARY KEY (student_id,session_id),"+
                "FOREIGN KEY (student_id) REFERENCES students (id),"+
                "FOREIGN KEY (session_id) REFERENCES course_session (id));";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(query);
        }catch (SQLException e ){
            System.out.println("erreur lors de la creation de la table enrollment"+ e.getMessage());
        }
    }

}
