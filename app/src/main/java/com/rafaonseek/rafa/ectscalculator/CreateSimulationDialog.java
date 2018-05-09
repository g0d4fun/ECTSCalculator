package com.rafaonseek.rafa.ectscalculator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.rafaonseek.rafa.ectscalculator.sqlite.DatabaseHandler;
import com.rafaonseek.rafa.ectscalculator.sqlite.Simulation;


/**
 * Created by Rafa on 2/20/2018.
 */

public class CreateSimulationDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final DatabaseHandler db;

    private Button submitBtn;

    public CreateSimulationDialog(@NonNull Context context) {
        super(context);

        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_simulation);

        submitBtn = (Button) findViewById(R.id.create_simulation_btn);

        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_simulation_btn:
                String name = ((EditText)findViewById(R.id.create_simulation_edit))
                        .getText().toString().trim();
                Simulation sim = new Simulation(-1,name,null);
                db.addSimulation(sim);
                break;
        }
        dismiss();
    }
}
