package fr.univ_amu.fr.formation.m1info.gla.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeacherManager implements Manager<Integer, Teacher> {

    private final Map<Integer, Teacher> teachers;
    public TeacherManager() {
        teachers = new ConcurrentHashMap<>();
    }
    @Override
    public void put(Integer key, Teacher value) {
        teachers.put(key, value);
    }

    @Override
    public Teacher get(Integer key) {
        return teachers.get(key);
    }

    @Override
    public void remove(Integer key) {
        teachers.remove(key);
    }

    @Override
    public Map<Integer, Teacher> getAll() {
        return new ConcurrentHashMap<>(teachers);
    }
}
