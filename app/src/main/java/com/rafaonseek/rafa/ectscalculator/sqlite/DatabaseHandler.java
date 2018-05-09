package com.rafaonseek.rafa.ectscalculator.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafa on 2/19/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECTSCalculator";

    private static final String TABLE_SIMULATIONS = "simulations";
    // Table Columns names
    private static final String SIMULATIONS_KEY_ID = "id";
    private static final String SIMULATIONS_KEY_NAME = "name";

    private static final String TABLE_COURSES = "courses";
    // Table Columns names
    private static final String COURSES_KEY_ID = "id";
    private static final String COURSES_KEY_ID_SIMULATION = "IdSim";
    private static final String COURSES_KEY_NAME = "name";
    private static final String COURSES_KEY_ECTS = "ects";
    private static final String COURSES_KEY_GRADE = "grade";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SIMULATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SIMULATIONS + "("
                + SIMULATIONS_KEY_ID + " INTEGER PRIMARY KEY,"
                + SIMULATIONS_KEY_NAME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SIMULATIONS_TABLE);
        String CREATE_COURSES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + "("
                + COURSES_KEY_ID + " INTEGER PRIMARY KEY,"
                + COURSES_KEY_ID_SIMULATION + " INTEGER,"
                + COURSES_KEY_NAME + " TEXT,"
                + COURSES_KEY_ECTS + " INTEGER,"
                + COURSES_KEY_GRADE + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_COURSES_TABLE);
        Log.i("ECTSCal",CREATE_SIMULATIONS_TABLE);
        Log.i("ECTSCal",CREATE_COURSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SIMULATIONS);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /**
     * Simulations Table
     **/
    public void addSimulation(Simulation sim) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SIMULATIONS_KEY_NAME, sim.getName());

        db.insert(TABLE_SIMULATIONS, null, values);
        db.close();
    }

    public Simulation getSimulation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SIMULATIONS, new String[]{SIMULATIONS_KEY_ID,
                        SIMULATIONS_KEY_NAME}, SIMULATIONS_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            Simulation sim = new Simulation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    getCoursesBySimulation(cursor.getInt(0))
            );
            return sim;
        }
        return null;
    }

    public List<Simulation> getAllSimulations() {
        List<Simulation> sims = new ArrayList<Simulation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SIMULATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Simulation sim = new Simulation(
                        cursor.getInt(0),
                        cursor.getString(1),
                        getCoursesBySimulation(cursor.getInt(0))
                );
                // Adding contact to list
                sims.add(sim);
            } while (cursor.moveToNext());
        }

        // return scores list
        return sims;
    }

    public int getSimulationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SIMULATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateSimulations(Simulation sim) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SIMULATIONS_KEY_NAME, sim.getName());

        // updating row
        return db.update(TABLE_SIMULATIONS, values, SIMULATIONS_KEY_ID + " = ?",
                new String[]{String.valueOf(sim.getId())});
    }

    public void deleteSimulation(Simulation score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SIMULATIONS, SIMULATIONS_KEY_ID + " = ?",
                new String[]{String.valueOf(score.getId())});
        db.close();
    }

    /**
     * Profiles Table
     **/
    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSES_KEY_ID_SIMULATION, course.getIdSim());
        values.put(COURSES_KEY_NAME, course.getName());
        values.put(COURSES_KEY_ECTS, course.getEcts());
        values.put(COURSES_KEY_GRADE, course.getGrade());

        db.insert(TABLE_COURSES, null, values);
        db.close();
    }

    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COURSES, new String[]{COURSES_KEY_ID,
                        COURSES_KEY_ID_SIMULATION,
                        COURSES_KEY_NAME,
                        COURSES_KEY_ECTS,
                        COURSES_KEY_GRADE},
                COURSES_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();

        Course course = new Course(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4));
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4));
                // Adding contact to list
                courses.add(course);
            } while (cursor.moveToNext());
        }

        // return courses list
        return courses;
    }

    public List<Course> getCoursesBySimulation(int idSimulation) {
        List<Course> courses = new ArrayList<Course>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, new String[]{COURSES_KEY_ID,
                        COURSES_KEY_ID_SIMULATION,
                        COURSES_KEY_NAME,
                        COURSES_KEY_ECTS,
                        COURSES_KEY_GRADE},
                COURSES_KEY_ID_SIMULATION + "=?",
                new String[]{String.valueOf(idSimulation)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4));
                // Adding contact to list
                courses.add(course);
            } while (cursor.moveToNext());
        }

        return courses;
    }

    public int getCoursesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COURSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        try {
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
            }
            return count;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSES_KEY_NAME, course.getName());
        values.put(COURSES_KEY_ECTS, course.getEcts());
        values.put(COURSES_KEY_GRADE, course.getGrade());

        // updating row
        return db.update(TABLE_COURSES, values, COURSES_KEY_ID + " = ?",
                new String[]{String.valueOf(course.getId())});
    }

    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSES, COURSES_KEY_ID + " = ?",
                new String[]{String.valueOf(course.getId())});
        db.close();
    }
}
