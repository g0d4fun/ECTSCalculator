package com.rafaonseek.rafa.ectscalculator.sqlite;

import java.util.List;

/**
 * Created by Rafa on 2/19/2018.
 */

public class Simulation {

    private final int id;
    private final String name;
    private final List<Course> courses;

    public Simulation(int id, String name, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
