package com.tara.attendanceforstudent.Activities.Subject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Activities.DashboardActivity;
import com.tara.attendanceforstudent.Activities.Student.AddNewStudentActivity;
import com.tara.attendanceforstudent.Activities.Student.AddStudentActivity;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.Models.SubjectModel;
import com.tara.attendanceforstudent.R;

import java.util.ArrayList;
import java.util.List;

public class AddNewSubjectsActivity extends AppCompatActivity {

    TextInputEditText subject;
    Spinner department, sem;
    ImageView save, back;
    FirebaseFirestore dbsubject;
    String sub, depart, seme;
    SubjectModel subjectModel;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_subjects);
        dbsubject = FirebaseFirestore.getInstance();
        initforaddnewsubject();
        SpinnerData();


    }

    private void SpinnerData() {


         collectionReference = dbsubject.collection("Department");
        final List<String> department1 = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, department1);
        collectionReference.orderBy("department_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String department_name = document.getString("department_name");
                        department1.add(department_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        department.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });



        collectionReference = dbsubject.collection("Sem");
        final List<String> sem1 = new ArrayList<>();
        final ArrayAdapter<String> addsem = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sem1);
        collectionReference.orderBy("sem_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String semName = document.getString("sem_name");
                        sem1.add(semName);
                        addsem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sem.setAdapter(addsem);
                    }
                    addsem.notifyDataSetChanged();
                }
            }
        });

    }

    private void initforaddnewsubject() {
        subject = findViewById(R.id.subject);
        department = findViewById(R.id.department);
        sem = findViewById(R.id.sem);
        save = findViewById(R.id.save);
        back = findViewById(R.id.ivback);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveinfirestore();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    private void saveinfirestore() {

        sub = subject.getText().toString().trim();
        depart = department.getSelectedItem().toString().trim();
        seme = sem.getSelectedItem().toString().trim();

        final DocumentReference documentReference = dbsubject.collection("Subject").document();


        subjectModel = new SubjectModel();
        subjectModel.setSubjectname(sub);
        subjectModel.setDepartment(depart);
        subjectModel.setSem(seme);


        dbsubject.collection("Subject").whereEqualTo("subjectname", sub).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(AddNewSubjectsActivity.this, "Already exist", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                if (task.getResult().size() == 0) {

                    documentReference.set(subjectModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", e.getMessage());

            }
        });
    }


}