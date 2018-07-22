package com.example.karan.digitalattendancesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartupActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_teacher, btn_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        btn_student = findViewById(R.id.starup_btn_student);
        btn_teacher = findViewById(R.id.startup_btn_teacher);

        btn_student.setOnClickListener(this);
        btn_teacher.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v== btn_student){
            Intent i = new Intent(this,StudentLoginActivity.class);
            startActivity(i);
            finish();
        }
        else if(v== btn_teacher){
            // go to teacher login activity
            Intent i = new Intent(this,TeacherLoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
