package com.rafaonseek.rafa.ectscalculator;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rafaonseek.rafa.ectscalculator.sqlite.Course;
import com.rafaonseek.rafa.ectscalculator.sqlite.DatabaseHandler;
import com.rafaonseek.rafa.ectscalculator.sqlite.Simulation;

import java.util.List;

/**
 * Created by Rafa on 2/19/2018.
 */

public class CoursesListAdapter extends BaseAdapter {

    private final Activity activity;
    private final DatabaseHandler db;
    private final int idSimulation;

    public CoursesListAdapter(Activity activity, int idSimulation) {
        this.activity = activity;
        this.db = new DatabaseHandler(activity);
        this.idSimulation = idSimulation;

        Simulation sim = db.getSimulation(idSimulation);
        ((TextView) activity.findViewById(R.id.message)).setVisibility(View.VISIBLE);
        ((TextView) activity.findViewById(R.id.message)).setText(sim == null ? "NULL" : sim.getName());
        ((TextView) activity.findViewById(R.id.num_courses)).setVisibility(View.VISIBLE);
        ((TextView) activity.findViewById(R.id.num_courses)).setText(sim == null ? "0" : sim.getCourses().size() + "");
    }

    @Override
    public int getCount() {
        List<Course> courses = db.getCoursesBySimulation(idSimulation);
        Log.i("ECTSCalculator", "Courses Size: " + courses.size());
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        List<Course> courses = db.getCoursesBySimulation(idSimulation);
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = activity.getLayoutInflater().inflate(R.layout.list_item_course, null);

        List<Course> courses = db.getCoursesBySimulation(idSimulation);
        final Course course = courses.get(position);
        String name = course.getName();
        int ects = course.getEcts();
        int grade = course.getGrade();

        EditText courseName = (layout.findViewById(R.id.item_course_edit));
        final EditText courseECTS = (layout.findViewById(R.id.item_ects_edit));
        EditText courseGrade = (layout.findViewById(R.id.item_grade_edit));

        courseName.setText(name);
        courseECTS.setText(ects + "");
        courseGrade.setText(grade + "");

        courseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Course toUpdate = new Course(
                        course.getId(),
                        course.getIdSim(),
                        s.toString(),
                        course.getEcts(),
                        course.getGrade());
                db.updateCourse(toUpdate);
            }
        });
        courseECTS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Course toUpdate = new Course(
                        course.getId(),
                        course.getIdSim(),
                        course.getName(),
                        Integer.parseInt(s.toString()),
                        course.getGrade());
                db.updateCourse(toUpdate);
            }
        });
        courseGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Course toUpdate = new Course(
                        course.getId(),
                        course.getIdSim(),
                        course.getName(),
                        course.getEcts(),
                        Integer.parseInt(s.toString()));
                db.updateCourse(toUpdate);
            }
        });

        ImageButton gradeRemove = layout.findViewById(R.id.item_grade_remove);
        gradeRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteCourse(course);
                notifyDataSetChanged();
            }
        });

        notifyDataSetChanged();
        return layout;
    }
}
