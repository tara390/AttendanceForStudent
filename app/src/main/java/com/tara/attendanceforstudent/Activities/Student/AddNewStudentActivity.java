package com.tara.attendanceforstudent.Activities.Student;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tara.attendanceforstudent.Activities.DashboardActivity;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.R;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddNewStudentActivity extends AppCompatActivity {

    ImageView ivback, ivdone, ivcaptureforstudent;
    TextInputEditText etfirstname, etlastname, etemail, etaddress, etparentmobile, emobile;
    TextView tvfingerprint;
    Spinner spdept, spdiv, spsem;
    FirebaseFirestore dbfirestore;
    String fname, lname, mobile, fingerprint, address, parentmobile, email, dept, div, sem, filepath;
    StudentModel studentmodel;
    BiometricPrompt biometricPrompt;
    Executor executor;
    AddNewStudentActivity activity = this;

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


        tvfingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    executor = Executors.newSingleThreadExecutor();
                    biometricPrompt = new BiometricPrompt.Builder(AddNewStudentActivity.this).setTitle(getString(R.string.fingerprint_header)).setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).build();


                    biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddNewStudentActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }


        });


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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databaos = baos.toByteArray();

            //set the image into imageview
            ivcaptureforstudent.setImageBitmap(bitmap);

            uploadimage(databaos);


        }


    }

    private void uploadimage(byte[] databaos) {

        if (databaos != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            //name of the image file (add time to have different files to avoid rewrite on the same file)

            String foldername = "Student";

            StorageReference imagesRef = storageRef.child("Student/" + UUID.randomUUID().toString());
            //send this name to database
            //upload image
            UploadTask uploadTask = imagesRef.putBytes(databaos);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    filepath = uri.toString();
                                    progressDialog.dismiss();
                                    //    Toast.makeText(AddNewStudentActivity.this, "file"+filepath, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                }
            });


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
        studentmodel.setImage(filepath);
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