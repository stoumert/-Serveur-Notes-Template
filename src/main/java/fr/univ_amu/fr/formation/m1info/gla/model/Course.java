package fr.univ_amu.fr.formation.m1info.gla.model;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaParser;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.SimpleFormulaFactory;
import fr.univ_amu.fr.formation.m1info.gla.technical.AntlrFormulaParser;

import java.util.Set;

/**
 * A classical university course
 */
public class Course {

    /**
     * The identifier of the course
     */
    private int id;

    /**
     * The name of the course
     */
    private final String name;

    /**
     * The description of the course
     */
    private final String description;

    /**
     * The number of credits provided by the course
     */
    private final int credits;

    /**
     * The formula for computing the grade from the grades
     */
    private final String formula;

    /**
     * The supervisor of the course
     */
    private final Teacher supervisor;

    /**
     * Constructor
     * @param id the identifier
     * @param name the name of the course
     * @param description the description of the course
     * @param credits the credits associated to the course
     * @param formula the formula for computing the grade
     * @param supervisor the course supervisor
     */
    public Course(int id, String name, String description, int credits, String formula, Teacher supervisor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.formula = formula;
        this.supervisor = supervisor;
    }

    /**
     * Get the identifier of the course
     * @return the identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Set the course identifier (if as -1)
     * @param id the identifier of the course
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
        else {
            throw new IllegalIdSettingException("Course id already set");
        }
    }

    /**
     * The name of the course
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The description of the course
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The number of credits (ECTS) for this course
     * @return the number of credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * The formula for computing the grades
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * The supervisor of the course
     * @return the supervisor
     */
    public Teacher getSupervisor() {
        return supervisor;
    }

    /**
     *
     * @return variable in formula
     */
    public Set<String> getViableCourseFormula(){
        FormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());
        CourseFormula courseFormula = parser.parse(formula);
        return courseFormula.getVariableNames();
    }
}
