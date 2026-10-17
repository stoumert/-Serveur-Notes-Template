package fr.univ_amu.fr.formation.m1info.gla.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StudentManager implements Manager<Integer , Student> {

    private final Map<Integer, Student> students;
    public StudentManager() {
        students = new ConcurrentHashMap<>();
    }
    @Override
    public void put(Integer key, Student value) {
        students.put(key, value);
    }

    @Override
    public Student get(Integer key) {
        return students.get(key);
    }

    @Override
    public void remove(Integer key) {
        students.remove(key);
    }

    @Override
    public Map<Integer, Student> getAll() {
        return new ConcurrentHashMap<>(students);
    }
}
