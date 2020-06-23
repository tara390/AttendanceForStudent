package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Models.TeacherModel;
import com.tara.attendanceforstudent.R;

import java.util.ArrayList;
import java.util.List;

public class TeacherRegistrationActivity extends AppCompatActivity {
    TextInputEditText etusername, etpassword, etfirstname, etlastname, etaddress, etmobile_no;
    Button btnsignup;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    String firstname, lastname, mobileno, address, username, password,dept;
    Spinner spdept;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);
        firestore = FirebaseFirestore.getInstance();
        initfornewteacherregistration();

    }


    @Override
    public void onBackPressed() {
        //Execute your code here
       Intent i=new Intent(TeacherRegistrationActivity.this,LoginActivity.class);
       startActivity(i);
       finish();

    }

    private void initfornewteacherregistration() {
        etfirstname = findViewById(R.id.edfirstname);
        etlastname = findViewById(R.id.edlastname);
        etmobile_no = findViewById(R.id.etmobile_no);
        etaddress = findViewById(R.id.etaddress);
        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        btnsignup = findViewById(R.id.btnsignup);
        spdept = findViewById(R.id.spDept);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        SpinnerData();


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstname = etfirstname.getText().toString();
                lastname = etlastname.getText().toString();
                mobileno = etmobile_no.getText().toString();
                address = etaddress.getText().toString();
                username = etusername.getText().toString();
                password = etpassword.getText().toString();
                dept=spdept.getSelectedItem().toString();
                savedatainclouddatabase();
            }
        });

    }

    private void SpinnerData() {

        CollectionReference collectionReference = firestore.collection("Department");
        final List<String> department = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, department);
        collectionReference.orderBy("department_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("department_name");
                        department.add(subject);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spdept.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void savedatainclouddatabase() {
        //  documentReference=firestore.collection("TeacherLogin");

        final String phone_no = "+" + "91" + mobileno;


        documentReference = firestore.collection("TeacherLogin").document();
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setFirstname(firstname);
        teacherModel.setLastname(lastname);
        teacherModel.setMobileno(phone_no);
        teacherModel.setAddress(address);
        teacherModel.setUsername(username);
        teacherModel.setPassword(password);
        teacherModel.setDept(dept);

        documentReference.set(teacherModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TeacherRegistrationActivity.this, "Data Save in cloud " + documentReference, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TeacherRegistrationActivity.this, VerificationOtpscreenActivity.class);
                i.putExtra("mobile_no", phone_no);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(String.valueOf(TeacherRegistrationActivity.this), "Fail in saving");

            }
        });


    }
}