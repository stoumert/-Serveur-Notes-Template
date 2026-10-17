package fr.univ_amu.fr.formation.m1info.gla.model;

import fr.univ_amu.fr.formation.m1info.gla.model.formulas.CourseFormula;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.FormulaParser;
import fr.univ_amu.fr.formation.m1info.gla.model.formulas.SimpleFormulaFactory;
import fr.univ_amu.fr.formation.m1info.gla.technical.AntlrFormulaParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A classical student
 */
public class Student {

    /**
     * The identifier of the student (-1 if not in database)
     */
    private int id;

    /**
     * The name of the student
     */
    private final String name;

    /**
     * The surname of the student
     */
    private final String surname;

    /**
     * The list of courses of the student
     */
    private final List<Course> courses;

    /**
     * The grades of the student
     */
    private final Map<Course, Map<String, Double>> marks;

    /**
     * Constructor
     * @param id the identifier of the student (-1 if not in database)
     * @param name the name of the student
     * @param surname the surname of the student
     * @param courses the list of courses follow by the student
     */
    public Student(int id, String name, String surname, List<Course> courses, Map<Course, Map<String, Double>> marks) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.courses = courses;
        this.marks = marks;
    }

    /**
     * Compute the grades of the student
     * @return the grade for each course
     */
    public Map<Course, Double> computeGrades () {
        Map<Course, Double> grades = new HashMap<>();

        for  (Course course : courses) {
            if (!marks.containsKey(course)) continue;
            Map<String, Double> mark = marks.get(course);
            String formula = course.getFormula();

            FormulaParser parser = new AntlrFormulaParser(new SimpleFormulaFactory());

            CourseFormula courseFormula = parser.parse(formula);
            double grade = courseFormula.getGrade(mark);
            grades.put(course, grade);
        }
        return grades;
    }

    /**
     * Add a mark from an exam for a given course
     * @param course the course
     * @param exam the name of the exam (should be coherent with the formula)
     * @param mark the mark
     */
    public void addMark(Course course, String exam, Double mark) {
        if (!courses.contains(course)) return;
        if (!marks.containsKey(course)) marks.put(course, new HashMap<>());
        marks.get(course).put(exam, mark);
    }

    /**
     * Get the identifier of the student
     * @return the id of the student
     */
    public int getId() {
        return id;
    }

    /**
     * Set the student identifier (if as -1)
     * @param id the identifier of the student
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
        else {
            throw new IllegalIdSettingException("Student id already set");
        }
    }

    /**
     * Get the name of the student
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the surname of the student
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Get the list of courses followed by the student
     * @return the list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Get the grades of the student (immutable)
     * @return the dictionary of grades
     */
    public Map<Course, Map<String, Double>> getMarks() {
        return Map.copyOf(marks);
    }

}
