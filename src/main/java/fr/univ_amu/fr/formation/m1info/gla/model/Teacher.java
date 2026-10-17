package fr.univ_amu.fr.formation.m1info.gla.model;

/**
 * A classical teacher
 */
public class Teacher {

    /**
     * The identifier of the teacher
     */
    private int id;

    /**
     * The name of the teacher
     */
    private final String name;

    /**
     * The surname of the teacher
     */
    private final String surname;

    /**
     * Constructor
     * @param id the identifier of the teacher
     * @param name the name of the teacher
     * @param surname the surname of the teacher
     */
    public Teacher(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Get the identifier of the teacher
     * @return the id of the teacher
     */
    public int getId() {
        return id;
    }

    /**
     * Set the teacher identifier (if as -1)
     * @param id the identifier of the teacher
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
        else {
            throw new IllegalIdSettingException("Teacher id already set");
        }
    }

    /**
     * Get the name of the teacher
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the surname of the teacher
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }
}
