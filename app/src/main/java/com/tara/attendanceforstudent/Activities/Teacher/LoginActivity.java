package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tara.attendanceforstudent.Activities.DashboardActivity;
import com.tara.attendanceforstudent.R;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edusername, edpassword;
    Button btnsignin;
    TextView tvregistrationforteacher,tvforgetPassword;
    FirebaseFirestore firestore;
    String username, password;
    private static final int PERMISSION_REQUEST_CODE = 200;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firestore = FirebaseFirestore.getInstance();
        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }

        init();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void init() {

        //For Login
        edusername = findViewById(R.id.edusername);
        edpassword = findViewById(R.id.edpassword);




        edpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    checkforloginindatabase();
                }


                return false;
            }
        });


        btnsignin = findViewById(R.id.btnlogin);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkforloginindatabase();


            }
        });


        //For Registration
        tvregistrationforteacher = findViewById(R.id.tvregistrationforteacher);
        tvregistrationforteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newteacherregistration = new Intent(LoginActivity.this, TeacherRegistrationActivity.class);
                startActivity(newteacherregistration);
                finish();
            }
        });



        //ForgetPassword
        tvforgetPassword=findViewById(R.id.tvforgetPassword);
        tvforgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = edusername.getText().toString().trim();
                Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                intent.putExtra("email",username);
                startActivity(intent);
            }
        });

    }

    private void checkforloginindatabase() {



        username = edusername.getText().toString().trim();
        password = edpassword.getText().toString().trim();
        firestore.collection("TeacherLogin").whereEqualTo("username", username).whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                Toast.makeText(LoginActivity.this, username + " " + "Login is Sucessfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Username and Password is invalid,Please try again Later", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Usernot exist", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}