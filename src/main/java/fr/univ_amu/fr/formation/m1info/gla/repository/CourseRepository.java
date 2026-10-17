package fr.univ_amu.fr.formation.m1info.gla.repository;

import fr.univ_amu.fr.formation.m1info.gla.data.CourseConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.CourseDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.CourseManager;
import fr.univ_amu.fr.formation.m1info.gla.model.Teacher;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCCourseDAO;

import java.util.List;
import java.util.Optional;

public class CourseRepository {
    private final JDBCCourseDAO jdbcDAO;
    private final CourseManager courseManager;
    private CourseConvertor convertor;


    public CourseRepository() {
        this.jdbcDAO = new JDBCCourseDAO();
        this.courseManager = new CourseManager();
    }
    private void loadData() {
        List<CourseDTO> courses = jdbcDAO.getAll();
        courses.forEach(course ->
            this.courseManager.put(course.id(), convertor.fromDTO(course)));
    }
    public Optional<CourseDTO> getById(int id) {
        return Optional.of(convertor.toDTO(courseManager.get(id)));
    }

    public List<CourseDTO> getAll() {
        return this.courseManager.getAll().values()
                .stream()
                .map(convertor::toDTO)
                .toList();
    }
    public CourseDTO create(CourseDTO course) {
        Integer id = jdbcDAO.create(course);
        CourseDTO persisted = course.clone(id);
        this.courseManager.put(id,convertor.fromDTO(persisted));
        return persisted;
    }

    public void update(CourseDTO course) {
        jdbcDAO.update(course);
        courseManager.remove(course.id());
        courseManager.put(course.id(), convertor.fromDTO(course));
    }
        public void delete(CourseDTO course) {
        jdbcDAO.delete(course);
        courseManager.remove(course.id());
    }
    public void setTeachersList(List<Teacher> teachers) {
        this.convertor = new CourseConvertor(teachers);
        loadData();
    }
    public CourseDTO create(CourseDTO course, List<Teacher> teacher) {
        Integer id = jdbcDAO.create(course);
        CourseDTO persisted = course.clone(id);
        CourseConvertor courseConvertor = new CourseConvertor(teacher);
        this.courseManager.put(id,courseConvertor.fromDTO(persisted));
        return persisted;
    }
}