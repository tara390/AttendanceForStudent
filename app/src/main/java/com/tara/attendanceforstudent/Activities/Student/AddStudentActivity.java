package com.tara.attendanceforstudent.Activities.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tara.attendanceforstudent.Activities.Teacher.TeacherRegistrationActivity;
import com.tara.attendanceforstudent.R;

public class AddStudentActivity extends AppCompatActivity {
    Button add_student, student_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        init();
    }

    private void init() {

        add_student = findViewById(R.id.add_student);
        student_search =findViewById(R.id.search_student);

        buttonClick();
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