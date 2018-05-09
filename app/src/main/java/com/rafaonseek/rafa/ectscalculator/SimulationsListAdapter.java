package com.rafaonseek.rafa.ectscalculator;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rafaonseek.rafa.ectscalculator.sqlite.Course;
import com.rafaonseek.rafa.ectscalculator.sqlite.DatabaseHandler;
import com.rafaonseek.rafa.ectscalculator.sqlite.Simulation;

import java.util.List;

/**
 * Created by Rafa on 2/19/2018.
 */

public class SimulationsListAdapter extends BaseAdapter {

    private final Activity activity;
    private final DatabaseHandler db;
    private Integer simulationIdSelected;

    public SimulationsListAdapter(Activity activity,Integer simulationIdSelected) {
        this.activity = activity;
        this.db = new DatabaseHandler(activity);
        this.simulationIdSelected = simulationIdSelected;
    }

    @Override
    public int getCount() {
        List<Simulation> simulations = db.getAllSimulations();
        return simulations.size();
    }

    @Override
    public Object getItem(int position) {
        List<Simulation> simulations = db.getAllSimulations();
        return simulations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View layout = activity.getLayoutInflater().inflate(R.layout.list_item_simulation, null);

        List<Simulation> sims = db.getAllSimulations();
        final Simulation sim = sims.get(position);
        List<Course> courses = db.getCoursesBySimulation(sim.getId());

        final String name = sim.getName();

        int sumEcts = 0;
        int average = 0;
        for (Course c : courses) {
            if (c.getEcts() > 0 && c.getGrade() >= 10 && c.getEcts() <= 20) {
                sumEcts += c.getEcts();
                average += (c.getEcts() * c.getGrade());
            }
        }
        if(sumEcts > 0)
            average = Math.round((float)(average / sumEcts));
        else
            average = 0;

        final EditText simulationNameEditTxt = ((EditText)layout.findViewById(R.id.item_simulation_edit));
        Button totalECTS = ((Button)layout.findViewById(R.id.item_sim_total_ects));
        Button gradesAverage = ((Button)layout.findViewById(R.id.item_sim_grade));

        simulationNameEditTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
                    //enter pressed
                    simulationNameEditTxt.setFocusable(false);
                    simulationNameEditTxt.setFocusableInTouchMode(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Simulation update = new Simulation(sim.getId(),s.toString(),null);
                db.updateSimulations(update);
            }
        });

        totalECTS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Simulation Selected", Toast.LENGTH_SHORT).show();
                simulationIdSelected = sim.getId();
                Intent intent = new Intent("broadcast_simulation");
                intent.putExtra("broadcast_simulation_id",simulationIdSelected + 1);
                activity.sendBroadcast(intent);
            }
        });

        gradesAverage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Simulation Selected", Toast.LENGTH_SHORT).show();
                simulationIdSelected = sim.getId();
                Intent intent = new Intent("broadcast_simulation");
                intent.putExtra("broadcast_simulation_id",simulationIdSelected);
                activity.sendBroadcast(intent);
            }
        });


        simulationNameEditTxt.setText(name);
        totalECTS.setText(sumEcts + " ");
        gradesAverage.setText(average + "");

        return layout;
    }
}
