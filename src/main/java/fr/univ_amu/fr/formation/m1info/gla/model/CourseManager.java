package fr.univ_amu.fr.formation.m1info.gla.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CourseManager implements Manager<Integer, Course> {
    private final Map<Integer, Course> courses;
    public CourseManager() {
        courses = new ConcurrentHashMap<>();
    }

    @Override
    public void put(Integer key, Course value) {
        courses.put(key, value);
    }

    @Override
    public Course get(Integer key) {
        return courses.get(key);
    }

    @Override
    public void remove(Integer key) {
        courses.remove(key);
    }

    @Override
    public Map<Integer, Course> getAll() {
        return new ConcurrentHashMap<>(courses);
    }

}
