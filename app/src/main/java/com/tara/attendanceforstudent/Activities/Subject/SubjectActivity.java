package com.tara.attendanceforstudent.Activities.Subject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tara.attendanceforstudent.R;

public class SubjectActivity extends AppCompatActivity {

    Button btnsubjectadd,btnsubjectsearch;
    ImageView ivsubjectmain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        initsubject();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    private void initsubject() {

        btnsubjectadd=findViewById(R.id.add_subject);
        btnsubjectsearch=findViewById(R.id.search_subject);

        ivsubjectmain=findViewById(R.id.ivsubjectmain);

        ivsubjectmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        btnsubjectadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SubjectActivity.this,AddNewSubjectsActivity.class);
                startActivity(i);
            }
        });

        btnsubjectsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SubjectActivity.this,ExistingSubjectActivity.class);
                startActivity(intent);
            }
        });
    }

}