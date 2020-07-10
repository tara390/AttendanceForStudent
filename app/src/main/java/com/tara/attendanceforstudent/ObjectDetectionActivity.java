package com.tara.attendanceforstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

public class ObjectDetectionActivity extends AppCompatActivity {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btndetect;
    ProgressDialog progressDialog;


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detection);

        init();


    }

    private void init() {
        cameraView = findViewById(R.id.cameraview);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        btndetect = findViewById(R.id.btndetect);

         progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait....");


        btndetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CameraStart();
            }
        });



        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override

            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                progressDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();

                facedetect(bitmap);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


    }

    private void facedetect(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder().build();
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {

                process(firebaseVisionFaces);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ObjectDetectionActivity.this, "Fail:" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void process(List<FirebaseVisionFace> firebaseVisionFaces) {

        int count=0;
        for (FirebaseVisionFace face:firebaseVisionFaces){
            Rect bounds=face.getBoundingBox();
            RectOverlay rectOverlay=new RectOverlay(graphicOverlay,bounds);
            graphicOverlay.add(rectOverlay);
            count++;
        }
        progressDialog.dismiss();
        Toast.makeText(this, "Detect Image"+count, Toast.LENGTH_SHORT).show();

    }

    private void CameraStart() {
        cameraView.start();
        cameraView.captureImage();
        graphicOverlay.clear();
    }
}