package com.tara.attendanceforstudent.Activities.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Activities.DashboardActivity;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.R;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddNewStudentActivity extends AppCompatActivity {

    ImageView ivback, ivdone, ivcaptureforstudent;
    TextInputEditText etfirstname, etlastname, etemail, etaddress, etparentmobile, emobile;
    TextView tvfingerprint;
    Spinner spdept, spdiv, spsem;
    FirebaseFirestore dbfirestore;
    String fname, lname, mobile, fingerprint, address, parentmobile, email, dept, div, sem;
    StudentModel studentmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);
        dbfirestore = FirebaseFirestore.getInstance();
        initforaddnewstudent();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddNewStudentActivity.this, AddStudentActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void initforaddnewstudent() {

        etfirstname = findViewById(R.id.etfirstname);
        etlastname = findViewById(R.id.etlastname);
        emobile = findViewById(R.id.edmobile_no);
        etparentmobile = findViewById(R.id.etparentsmobileno);
        etaddress = findViewById(R.id.edaddress);
        etemail = findViewById(R.id.etemail);
        tvfingerprint = findViewById(R.id.tvfigerprint);
        ivback = findViewById(R.id.ivbackpressed);
        ivdone = findViewById(R.id.ivdone);
        ivcaptureforstudent = findViewById(R.id.ivcaptureforstudent);
        spdept = findViewById(R.id.spdepartment);
        spsem = findViewById(R.id.spsem);
        spdiv = findViewById(R.id.spDiv);


        spinnerData();


        ivcaptureforstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opencamera();
            }
        });

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillupallthedatacompalsury();
            }
        });


    }

    private void Opencamera() {

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, 1);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databaos = baos.toByteArray();

            //set the image into imageview
            ivcaptureforstudent.setImageBitmap(bitmap);

        }


    }

        private void spinnerData() {

        //Department Data save in Firestore
        CollectionReference collectionReference = dbfirestore.collection("Department");
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
                        spdept.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        //Division Data save in Firestore
        collectionReference = dbfirestore.collection("Div");
        final List<String> division = new ArrayList<>();
        final ArrayAdapter<String> addiv = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, division);
        collectionReference.orderBy("division_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String div = document.getString("division_name");
                        division.add(div);
                        addiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spdiv.setAdapter(addiv);
                    }
                    addiv.notifyDataSetChanged();
                }
            }
        });

        //Sem data save in firestore
        collectionReference = dbfirestore.collection("Sem");
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
                        spsem.setAdapter(addsem);
                    }
                    addsem.notifyDataSetChanged();
                }
            }
        });


    }

    private void fillupallthedatacompalsury() {
        fname = etfirstname.getText().toString();
        lname = etlastname.getText().toString();
        mobile = emobile.getText().toString();
        parentmobile = etparentmobile.getText().toString();
        address = etaddress.getText().toString();
        email = etemail.getText().toString();
        dept = spdept.getSelectedItem().toString();
        div = spdiv.getSelectedItem().toString();
        sem = spsem.getSelectedItem().toString();


        DocumentReference documentReference = dbfirestore.collection("Student").document();

        studentmodel = new StudentModel();
        studentmodel.setFirstname(fname);
        studentmodel.setLastname(lname);
        studentmodel.setMobile(mobile);
        studentmodel.setParentmobile(parentmobile);
        studentmodel.setEmail(email);
        studentmodel.setAddress(address);
        studentmodel.setDept(dept);
        studentmodel.setDiv(div);
        studentmodel.setSem(sem);
//        studentmodel.setFingerprint(fingerprint);

        documentReference.set(studentmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddNewStudentActivity.this, "Store db sucessful in firstore" + studentmodel, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewStudentActivity.this, "Fail entry in student", Toast.LENGTH_SHORT).show();
            }
        });

    }


}