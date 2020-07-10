package com.tara.attendanceforstudent.Activities.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tara.attendanceforstudent.Activities.Teacher.TeacherRegistrationActivity;
import com.tara.attendanceforstudent.R;

public class AddStudentActivity extends AppCompatActivity {
    Button add_student, student_search;
    ImageView ivstudentmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        init();
    }

    private void init() {

        ivstudentmain=findViewById(R.id.ivstudentmain);
        ivstudentmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        add_student = findViewById(R.id.add_student);
        student_search =findViewById(R.id.search_student);

        buttonClick();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    private void buttonClick() {
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddStudentActivity.this, AddNewStudentActivity.class);
                startActivity(i);
            }

        });
        student_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStudentActivity.this, ExisitingStudentActivity.class);
                startActivity(intent);
            }
        });
    }
}