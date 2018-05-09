package com.rafaonseek.rafa.ectscalculator.sqlite;

/**
 * Created by Rafa on 2/19/2018.
 */

public class Course implements Cloneable{

    private final int id;
    private final int idSimulation;
    private final String name;
    private final int ects;
    private final int grade;

    public Course(int id, int idSimulation, String name, int ects, int grade) {
        this.id = id;
        this.idSimulation = idSimulation;
        this.name = name;
        this.ects = ects;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getIdSim() {
        return idSimulation;
    }

    public String getName() {
        return name;
    }

    public int getEcts() {
        return ects;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public Course clone() throws CloneNotSupportedException {
        Course course = new Course(getId(),getIdSim(),getName(),getEcts(),getGrade());
        return course;
    }
}
