package com.rafaonseek.rafa.ectscalculator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rafaonseek.rafa.ectscalculator.sqlite.Course;
import com.rafaonseek.rafa.ectscalculator.sqlite.DatabaseHandler;

/**
 * Created by Rafa on 2/20/2018.
 */

public class CreateCourseDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final DatabaseHandler db;
    private final int simulationId;

    private Button submitBtn;

    public CreateCourseDialog(@NonNull Context context,int simulationId) {
        super(context);

        this.simulationId = simulationId;
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_course);

        submitBtn = (Button) findViewById(R.id.create_course_btn);

        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_course_btn:
                String name = ((EditText)findViewById(R.id.create_course_title))
                        .getText().toString().trim();
                String ectsStr = ((EditText)findViewById(R.id.create_course_ects))
                        .getText().toString().trim();
                String gradeStr = ((EditText)findViewById(R.id.create_course_grade))
                        .getText().toString().trim();

                if(!Utils.tryParseInt(ectsStr) || !Utils.tryParseInt(gradeStr)){
                    Toast.makeText(context, "ECTS and Grade must be Numeric.", Toast.LENGTH_LONG).show();
                    return;
                }
                int ects = Integer.parseInt(ectsStr);
                int grade = Integer.parseInt(gradeStr);
                if(ects < 0 || ects > 100){
                    Toast.makeText(context, "ECTS out of bound. Set a number between 0 and 100.", Toast.LENGTH_LONG).show();
                    return;
                }else if(grade < 10 || grade > 20){
                    Toast.makeText(context, "Grade must be Numeric between 10 and 20.", Toast.LENGTH_LONG).show();
                    return;
                }

                Course create = new Course(-1,simulationId,name,ects,grade);
                db.addCourse(create);
                break;
        }
        dismiss();
    }
}
