package com.tara.attendanceforstudent.Activities.Subject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Activities.Student.Adapters.StudentAdapter;
import com.tara.attendanceforstudent.Activities.Subject.Adapter.SubjectAdapter;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.Models.SubjectModel;
import com.tara.attendanceforstudent.R;

import java.util.ArrayList;
import java.util.List;

public class ExistingSubjectActivity extends AppCompatActivity {

    ImageView ivbackforsubject;
    RecyclerView rvsubject;
    Button btnsearch;
    Spinner sdepartment, ssem;
    FirebaseFirestore dbforsubject;
    String department, sem;
    List<SubjectModel> subjectModels;
    SubjectAdapter subjectAdapter;
    SubjectModel subjectModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_subject);
        dbforsubject = FirebaseFirestore.getInstance();

        initSubject();


        RecyclerViewListDataforSubject();

    }

    private void RecyclerViewListDataforSubject() {


        dbforsubject.collection("Subject")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            subjectModels = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {

                                SubjectModel note = doc.toObject(SubjectModel.class);
                                note.setSubjectname(doc.getString("subjectname"));
                                note.setDepartment(doc.getString("department"));
                                note.setSem(doc.getString("sem"));
                                subjectModels.add(note);
                            }

                            rvsubject = findViewById(R.id.rvsubject);

                            subjectAdapter = new SubjectAdapter(subjectModels, getApplicationContext(), dbforsubject);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rvsubject.setLayoutManager(mLayoutManager);
                            rvsubject.setItemAnimator(new DefaultItemAnimator());
                            rvsubject.setAdapter(subjectAdapter);


                        }


                    }
                });


    }

    private void spinnerData() {

        //Department Data save in Firestore
        CollectionReference collectionReference = dbforsubject.collection("Department");
        final List<String> department = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, department);
        collectionReference.orderBy("department_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String department_name = document.getString("department_name");
                        department.add(department_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sdepartment.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        //Sem data save in firestore
        collectionReference = dbforsubject.collection("Sem");
        final List<String> sem = new ArrayList<>();
        final ArrayAdapter<String> addsem = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sem);
        collectionReference.orderBy("sem_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String semName = document.getString("sem_name");
                        sem.add(semName);
                        addsem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ssem.setAdapter(addsem);
                    }
                    addsem.notifyDataSetChanged();
                }
            }
        });


    }

    private void initSubject() {


        ivbackforsubject = findViewById(R.id.ivsubjectback);

        btnsearch = findViewById(R.id.btnsubject);
        sdepartment = findViewById(R.id.sdepartment);
        ssem = findViewById(R.id.sSem);


        spinnerData();

        ivbackforsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }


        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchData();
            }
        });

    }

    private void SearchData() {

        department = sdepartment.getSelectedItem().toString();
        sem = ssem.getSelectedItem().toString();


        dbforsubject.collection("Subject").whereEqualTo("department", department).whereEqualTo("sem", sem).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    subjectModels = new ArrayList<>();

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {



                        rvsubject.setVisibility(View.VISIBLE);
                        subjectModel = documentSnapshot.toObject(SubjectModel.class);
                        subjectModel.setSubjectname(documentSnapshot.getString("subjectname"));

                        subjectModels.add(subjectModel);

                        subjectAdapter.filterNames(subjectModels);


                    }

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ExistingSubjectActivity.this, "Failure" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }
}