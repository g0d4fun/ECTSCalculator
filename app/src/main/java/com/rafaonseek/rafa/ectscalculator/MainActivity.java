package com.rafaonseek.rafa.ectscalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView list;
    private boolean onCourses;
    private Integer simulationIdSelected;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_simulations:
                    launchSimulations();
                    return true;
                case R.id.navigation_courses:
                    Log.i("ECTSCalculator","Sim Selected ID:" + simulationIdSelected);
                    if(!launchCourses(simulationIdSelected))
                        return false;
                    return true;
            }
            return false;
        }
    };

    protected void launchSimulations() {
        mTextMessage.setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.num_courses)).setVisibility(View.INVISIBLE);
        list.setAdapter(new SimulationsListAdapter(this,simulationIdSelected));
        onCourses = false;
    }

    protected boolean launchCourses(int simulationId) {
        if(simulationId < 0){
            Toast.makeText(this, "First must choose a Simulation.", Toast.LENGTH_SHORT).show();
            return false;
        }
        mTextMessage.setText(R.string.courses);
        list.setAdapter(new CoursesListAdapter(this, simulationId));
        onCourses = true;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.num_courses)).setVisibility(View.INVISIBLE);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        simulationIdSelected = -1;
        list = (ListView) this.findViewById(R.id.list_view);
        list.setAdapter(new SimulationsListAdapter(this,simulationIdSelected));
        onCourses = false;

        BroadcastReceiver updateCourseBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent != null){
                    simulationIdSelected = intent.getIntExtra("broadcast_simulation_id",-1);
                    Log.i("ECTSCalculator","BroadCast Received");
                    launchCourses(simulationIdSelected);
                    navigation.setSelectedItemId(R.id.navigation_courses);
                }
            }
        };
        IntentFilter filter = new IntentFilter("broadcast_simulation");
        registerReceiver(updateCourseBr,filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                if (onCourses) {
                    CreateCourseDialog dlg = new CreateCourseDialog(this,simulationIdSelected);
                    dlg.show();
                } else {
                    CreateSimulationDialog dlg = new CreateSimulationDialog(this);
                    dlg.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(onCourses){
            navigation.setSelectedItemId(R.id.navigation_simulations);
            launchSimulations();
        }
        //super.onBackPressed();
    }
}
