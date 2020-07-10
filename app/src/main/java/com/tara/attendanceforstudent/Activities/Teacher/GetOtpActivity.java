package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tara.attendanceforstudent.Activities.DashboardActivity;
import com.tara.attendanceforstudent.R;

import java.util.concurrent.TimeUnit;

public class GetOtpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String mVerificationId, mobile;
    EditText edverificationcode;
    ProgressBar progressBar;
    TextView tvphoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);

        mAuth = FirebaseAuth.getInstance();


        edverificationcode = findViewById(R.id.otpet);
        mobile = getIntent().getStringExtra("phoneno");

        tvphoneno = findViewById(R.id.tvmobileno);
        tvphoneno.setText(mobile);
        sendVerificationcode(mobile);

        findViewById(R.id.btngetandverified).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = edverificationcode.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    edverificationcode.setError("Enter Code...");
                    edverificationcode.requestFocus();
                    return;
                }
                Verifycode(code);
            }
        });

    }

    private void Verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        SigninwihCredential(credential);
    }

    private void SigninwihCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(GetOtpActivity.this, ChangePasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(GetOtpActivity.this, "Error in OnComplete...!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendVerificationcode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edverificationcode.setText(code);
                Verifycode(code);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(GetOtpActivity.this, "Error Ocurred" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}