package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.tara.attendanceforstudent.R;

import static com.tara.attendanceforstudent.R.drawable.ic_baseline_done_24;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText ednewpassword, edconfirmpassword;
    ImageView ivchangedpassword, passwordtoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
    }

    private void init() {

        ednewpassword = findViewById(R.id.ednewpassword);
        edconfirmpassword = findViewById(R.id.edconfirmpassword);

        passwordtoogle = findViewById(R.id.password_toggle);


        edconfirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newpassword = ednewpassword.getText().toString();

                if (newpassword.equals(edconfirmpassword.getText().toString())) {


                    passwordtoogle.setVisibility(View.VISIBLE);
                    Toast.makeText(ChangePasswordActivity.this, "Password Match Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    passwordtoogle.setVisibility(View.GONE);
                }

            }
        });
    }
}