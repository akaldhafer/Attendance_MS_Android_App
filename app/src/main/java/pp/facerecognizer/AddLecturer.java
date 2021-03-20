package pp.facerecognizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

import pp.facerecognizer.Lecturer.LecturerView;
import pp.facerecognizer.Lecturer.Lecturermp;

public class AddLecturer extends AppCompatActivity implements LecturerView {
    private static final String TAG = "AddLecturer";
    private EditText TeacherName, TeacherID, Teacherpassword, lecturerEmail;
    private Lecturermp teachermp;
    public ArrayList<String> lecturerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        lecturerEmail =  findViewById(R.id.edTeacherEmail);
        teachermp = new Lecturermp(this);
        TeacherName = findViewById(R.id.TeacherName);
        TeacherID = findViewById(R.id.TeacherID);
        Teacherpassword = findViewById(R.id.TeacherPassword);
    }
    private void initSignUp( String name, String mID, String password , String email , String token) {
        teachermp.onSuccessUpdate(AddLecturer.this, name, mID, password,email, token);
    }
    private void checkSignUpDetails() {
        String sName = TeacherName.getText().toString().trim();
        String sID = TeacherID.getText().toString().trim();
        String Password = Teacherpassword.getText().toString().trim();
        String email = lecturerEmail.getText().toString().trim();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(!TextUtils.isEmpty(sName)
                && !TextUtils.isEmpty(Password)
                && !TextUtils.isEmpty(sID)
                && !TextUtils.isEmpty(email)){
            //check id if has been used before
            if (lecturerList.contains(sID)){
                Toast.makeText(AddLecturer.this, "The ID has been used before, Please change it", Toast.LENGTH_LONG).show();
            }
            else{
                //send record to be saved
                initSignUp(sName, sID, Password, email, timestamp.toString()); }
        }else{
            if(TextUtils.isEmpty(sName)){
                TeacherName.setError("Name is required");
                return;
            }if(TextUtils.isEmpty(sID)){
                TeacherID.setError("ID is required");
                return;
            }if (TextUtils.isEmpty(Password)){
                Teacherpassword.setError("Password is required ");
            }
            if(TextUtils.isEmpty(email)){
                lecturerEmail.setError("Email is required");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkID();
    }

    private void checkID(){
        FirebaseFirestore.getInstance().collection("TeacherData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(int i = 0; i < task.getResult().size(); i++){
                        String lectID= task.getResult().getDocuments().get(i).getString("teacherID").toString();
                        lecturerList.add(lectID);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLecturer.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onUpdateUpuccess(String message) {
        Toast.makeText(AddLecturer.this, "Lecturer has been added successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AddLecturer.this, AdminDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(AddLecturer.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddLecturer.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }

    public void AddTeacher(View view) {
        checkSignUpDetails();
    }
}
