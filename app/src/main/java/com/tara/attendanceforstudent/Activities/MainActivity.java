package com.tara.attendanceforstudent.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tara.attendanceforstudent.Activities.Teacher.LoginActivity;
import com.tara.attendanceforstudent.R;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    ImageView ivlogo;
    int SPLASH_DISPLAY_LENGTH = 3000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //making this activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        init();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startSecondActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



    private void startSecondActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this, ivlogo, ViewCompat.getTransitionName(ivlogo));
        startActivity(intent, options.toBundle());
        finish();


    }


    private void init() {

        ivlogo = findViewById(R.id.ivlogo);


    }
}