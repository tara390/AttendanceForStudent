package com.tara.attendanceforstudent.Activities.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Activities.Student.Adapters.StudentAdapter;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.R;

import java.util.ArrayList;
import java.util.List;

public class ExisitingStudentActivity extends AppCompatActivity {

    SearchView searchviewforstudent;
    ImageView ivstudentback;
    RecyclerView rvlistforstudent;
    FirebaseFirestore dbstudent;
    StudentAdapter studentAdapter;
    List<StudentModel> studentmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exisiting_student);
        dbstudent = FirebaseFirestore.getInstance();
        initforstudent();
        setListforStudent();

    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        super.onBackPressed();
    }

    private void setListforStudent() {


        dbstudent.collection("Student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            studentmodel = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {

                                StudentModel note = doc.toObject(StudentModel.class);
                                note.setFirstname(doc.getString("firstname"));
                                note.setLastname(doc.getString("lastname"));
                                note.setMobile(doc.getString("mobile"));
                                note.setParentmobile(doc.getString("parentmobile"));
                                studentmodel.add(note);
                            }

                            rvlistforstudent = findViewById(R.id.rvlistforstudent);

                            studentAdapter = new StudentAdapter(studentmodel, getApplicationContext(), dbstudent);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rvlistforstudent.setLayoutManager(mLayoutManager);
                            rvlistforstudent.setItemAnimator(new DefaultItemAnimator());
                            rvlistforstudent.setAdapter(studentAdapter);


                        }


                    }
                });


    }

    private void initforstudent() {
        //Back button functionality
        ivstudentback = findViewById(R.id.ivstudentback);
        ivstudentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Search Functionlity
        searchviewforstudent = findViewById(R.id.searchviewforstudent);
        searchviewforstudent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                processQuery(s);

                if (s.isEmpty()) {
                    rvlistforstudent.setVisibility(View.GONE);


                } else {

                    rvlistforstudent.setVisibility(View.VISIBLE);
                }


                return true;
            }
        });

    }

    private void processQuery(String s) {

        List<StudentModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (StudentModel data : studentmodel) {
            if (data.getFirstname().contains(s)||data.getLastname().contains(s)||data.getDept().contains(s)) {

                if (s.length()>=3)
                {
                    filterdNames.add(data);
                }


            }
            studentAdapter.searchfilter(filterdNames);

        }


    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();


        return super.onCreateOptionsMenu(menu);
    }*/


}