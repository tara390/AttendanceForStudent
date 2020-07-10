package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    ImageView ivforgetback, verified;
    TextInputEditText edmobileno;
    Button btngetotp;
    String mobileno;
    FirebaseFirestore firestore;
    String phone_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        firestore = FirebaseFirestore.getInstance();


        initforgetPassword();


    }

    private void initforgetPassword() {

        edmobileno = findViewById(R.id.edemailid);
        btngetotp = findViewById(R.id.btngetotp);
        ivforgetback = findViewById(R.id.ivforgetpass);
        verified = findViewById(R.id.verified);


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        //email id check whether it is exist on firestore database or not...!!
        edmobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mobileno = edmobileno.getText().toString();
                checkwithdatabase(mobileno);
                //Toast.makeText(ForgetPasswordActivity.this, "Valid Email id"+email, Toast.LENGTH_SHORT).show();


            }
        });

        btngetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ForgetPasswordActivity.this, GetOtpActivity.class);
                i.putExtra("phoneno", phone_no);
                startActivity(i);
            }
        });


        ivforgetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


    }

    private void checkwithdatabase(String editable) {

        phone_no = "+" + "91" + editable;


        firestore.collection("TeacherLogin").whereEqualTo("mobileno", phone_no).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {

                    for (DocumentSnapshot doc : task.getResult()) {

                        if (doc.exists()) {
                            verified.setVisibility(View.VISIBLE);
                        }


                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "InValid Email id", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, "Failure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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