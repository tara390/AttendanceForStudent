package com.tara.attendanceforstudent.Activities.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tara.attendanceforstudent.Activities.Student.AddNewStudentActivity;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.Models.TeacherModel;
import com.tara.attendanceforstudent.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeacherRegistrationActivity extends AppCompatActivity {
    TextInputEditText etusername, etpassword, etfirstname, etlastname, etaddress, etmobile_no;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    String firstname, lastname, mobileno, address, username, password,dept,filepath;
    Spinner spdept;
    ImageView back,done;
    CircleImageView ivcaptureforteacher;

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

        ivcaptureforteacher=findViewById(R.id.ivcaptureforteacher);

        ivcaptureforteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opencamera();
            }
        });


        spdept = findViewById(R.id.spDept);
        back=findViewById(R.id.back);
        done=findViewById(R.id.done);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        SpinnerData();


        done.setOnClickListener(new View.OnClickListener() {
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
            ivcaptureforteacher.setImageBitmap(bitmap);

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



            StorageReference imagesRef = storageRef.child("Teacher/" + UUID.randomUUID().toString());
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



        final String phone_no = "+" + "91" + mobileno;


        documentReference = firestore.collection("TeacherLogin").document();
        final TeacherModel teacherModel = new TeacherModel();
        teacherModel.setFirstname(firstname);
        teacherModel.setLastname(lastname);
        teacherModel.setMobileno(phone_no);
        teacherModel.setAddress(address);
        teacherModel.setUsername(username);
        teacherModel.setPassword(password);
        teacherModel.setDept(dept);

        firestore.collection("TeacherLogin").whereEqualTo("firstname", firstname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(TeacherRegistrationActivity.this, "Already exist", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                if (task.getResult().size() == 0) {

                    documentReference.set(teacherModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            etfirstname.setText("");
                            etlastname.setText("");
                            etusername.setText("");
                            etmobile_no.setText("");
                            etpassword.setText("");
                            etaddress.setText("");
                            spdept.setSelected(false);

                            Intent i = new Intent(TeacherRegistrationActivity.this, VerificationOtpscreenActivity.class);
                            i.putExtra("mobile_no", phone_no);
                            startActivity(i);
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", e.getMessage());

            }
        });
    }




    }
