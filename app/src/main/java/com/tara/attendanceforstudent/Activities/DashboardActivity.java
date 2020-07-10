package com.tara.attendanceforstudent.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tara.attendanceforstudent.Activities.Student.AddStudentActivity;
import com.tara.attendanceforstudent.Activities.Subject.AddNewSubjectsActivity;
import com.tara.attendanceforstudent.Activities.Subject.SubjectActivity;
import com.tara.attendanceforstudent.Activities.Teacher.ViewAnimation;
import com.tara.attendanceforstudent.R;
import com.tara.attendanceforstudent.ui.StudentReport.StudentReportFragment;
import com.tara.attendanceforstudent.ui.TakeAttendance.TakeAttendanceFragment;
import com.tara.attendanceforstudent.ui.ClassReport.ClassReportFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    Boolean isRotate = false;
    FloatingActionButton fabadd, fabaddsubject, fabaddstudent;
    Fragment fragment = null;
    BottomNavigationView navView;
    ImageView ivdashboardback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        loadFragment(new Fragment());

        navView = findViewById(R.id.nav_view);


        navView.setOnNavigationItemSelectedListener(this);

        Floatinginit();

        ivdashboardback=findViewById(R.id.ivdashboardback);
        ivdashboardback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Floatinginit() {

        fabadd = findViewById(R.id.fabadd);
        fabaddstudent = findViewById(R.id.fabaddstudent);
        fabaddsubject = findViewById(R.id.fabaddsubject);

        ViewAnimation.init(fabaddstudent);
        ViewAnimation.init(fabaddsubject);


        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRotate = ViewAnimation.rotateFab(view, !isRotate);
                if (isRotate) {
                    ViewAnimation.showIn(fabaddstudent);
                    ViewAnimation.showIn(fabaddsubject);

                } else {
                    ViewAnimation.showOut(fabaddstudent);
                    ViewAnimation.showOut(fabaddsubject);
                }
            }
        });



        fabaddstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashboardActivity.this, AddStudentActivity.class);
                startActivity(i);
            }
        });

     /*   fabaddstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new StudentFragment();
                // getFragmentManager().popBackStack();
               *//* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);

                transaction.addToBackStack("Student Fragment");

                transaction.commit();*//*

                FrameLayout fl = (FrameLayout) findViewById(R.id.nav_host_fragment);
                fl.removeAllViews();
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.add(R.id.container, fragment);
                transaction1.commit();


            }
        });

*/

        fabaddsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashboardActivity.this, SubjectActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack("Dashboard Fragment")
                    .commit();

            return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int size = navView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navView.getMenu().getItem(i).setChecked(false);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new TakeAttendanceFragment();
                    break;

                case R.id.navigation_dashboard:
                    fragment = new StudentReportFragment();
                    break;

                case R.id.navigation_notifications:
                    fragment = new ClassReportFragment();

                    break;

            }


        }

        return loadFragment(fragment);
    }
}