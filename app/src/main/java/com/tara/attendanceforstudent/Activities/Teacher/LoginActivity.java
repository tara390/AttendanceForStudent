package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    TextView tvregistrationforteacher;
    FirebaseFirestore firestore;
    String username, password;


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firestore = FirebaseFirestore.getInstance();
        init();

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