package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import pp.facerecognizer.Students.StudentModules;
import pp.facerecognizer.Students.StudentView;
import pp.facerecognizer.Students.Studentmp;

public class AddStudents extends AppCompatActivity implements StudentView {
    private EditText studentName, studentId, studentPW;
    private EditText studentCourse;
    public  String sImageUri;
    private ImageView imageView;
    public Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private StorageTask uploadtask;
    public ArrayList<String> studentList = new ArrayList<>();
    private static final String TAG = "AddStudents";

    ProgressDialog mProgressDialog;
    Studentmp studentmp;

    public AddStudents() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkID();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        studentName = findViewById(R.id.StudentName);
        studentId = findViewById(R.id.StudentID);
        studentPW = findViewById(R.id.StudentPasswd);
        studentCourse= findViewById(R.id.StudentCourse);
        studentmp = new Studentmp(this);

        imageView = (ImageView) findViewById(R.id.ChosenImage);

        storageReference = FirebaseStorage.getInstance().getReference("StudentImage");
        //
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait, Creating A student profile..");

       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddStudents.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddStudents.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    PickerImage();
                }
            }
        });

    }

    private void checkID(){
        FirebaseFirestore.getInstance().collection("StudentData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(int i = 0; i < task.getResult().size(); i++){
                        String studId= task.getResult().getDocuments().get(i).getString("studentID").toString();
                        studentList.add(studId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStudents.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private  void uploadFile(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading the image...");
        pd.show();
        Log.d(TAG, "uploadfile: getLastPathSegment type " + imageUri.getLastPathSegment());
        if(imageUri != null){

            StorageReference fileReference =
                    storageReference.child(imageUri.getLastPathSegment());
            uploadtask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            sImageUri = uri.toString();
                            Log.d(TAG, "uploadFile: url will be upload " + sImageUri);
                            checkSignUpDetails(sImageUri);
                            Toast.makeText(AddStudents.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStudents.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    pd.setMessage("Progress: "+ (int) progress + "%");
                }
            });

        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==13 && resultCode==RESULT_OK && data!=null) {
            imageUri = data.getData();

            Log.d(TAG, "onActivityResult: url is " + imageUri);
            imageView.setImageURI(imageUri);

        }
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                Log.d(TAG, "onActivityResult: url is " + imageUri);
                Picasso.with(AddStudents.this).load(result.getUri()).into(imageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error.getMessage().toString());
            }
        }
    }
    private void PickerImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AddStudents.this);
    }


    public void AddStudents(View view) {
        if(uploadtask != null && uploadtask.isInProgress()){
            Toast.makeText(AddStudents.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        }
        uploadFile();
    }

    private void initSignUp( String name, String mID, String password , String course , String time, String imageUri) {
        mProgressDialog.show();
        studentmp.onSuccessUpdate(AddStudents.this, name, mID, password,course, time, imageUri);
    }
    private void checkSignUpDetails(String imageuri) {
        String sName = studentName.getText().toString().trim();
        String sID = studentId.getText().toString().trim();
        String sPassword = studentPW.getText().toString().trim();
        String sCourse = studentCourse.getText().toString().trim();
        Log.d(TAG, "checkdetails: url before upload " + imageuri);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(!TextUtils.isEmpty(sName)
                && !TextUtils.isEmpty(sPassword)
                && !TextUtils.isEmpty(sID)
                && !TextUtils.isEmpty(sCourse)
                && imageuri != null){
            //check id if has been used before
            if (studentList.contains(sID)){
                Toast.makeText(AddStudents.this, "The ID has been used before, Please change it", Toast.LENGTH_LONG).show();
            }
            else{
                //send record to be saved
                initSignUp(sName, sID, sPassword, sCourse, timestamp.toString(), imageuri); }

        }else{
            if(TextUtils.isEmpty(sName)){
                studentName.setError("Student Name is required");
                return;
            }if(TextUtils.isEmpty(sID)){
                studentId.setError("Student ID is required");
                return;
            }if (TextUtils.isEmpty(sCourse)){
                studentCourse.setError("Student course is required");
                return;

            }if (TextUtils.isEmpty(sPassword)){
                studentPW.setError("Student Password is required");
                return;
            }
            if (imageuri == null){
                Toast.makeText(AddStudents.this, "Please upload student image first", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onUpdateUpuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(AddStudents.this, "Student has been added", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddStudents.this, AdminDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public void onUpdateFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(AddStudents.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddStudents.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
