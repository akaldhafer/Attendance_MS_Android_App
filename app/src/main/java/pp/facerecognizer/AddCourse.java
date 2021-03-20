package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

import pp.facerecognizer.Module.ModuleImp;
import pp.facerecognizer.Module.ModuleView;

public class AddCourse extends AppCompatActivity implements ModuleView {
    private EditText MName, mID, Level,moduleLeader;
    private Button PushData;
    private ModuleImp sginUpImp;
    ProgressDialog mProgressDialog;
    public ArrayList<String> courseList = new ArrayList<>();
    private static final String TAG = "AddCourse";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        sginUpImp = new ModuleImp(this);
        MName = findViewById(R.id.et_name);
        mID = findViewById(R.id.et_email);
        Level = findViewById(R.id.et_password);
        moduleLeader = findViewById(R.id.et_repassword);
        PushData = findViewById(R.id.btn_register);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait, creating an module ..");
        PushData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSignUpDetails();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkID();
    }

    private void initSignUp(String name, String mID, String Token , String Level , String mLeaderm) {
        mProgressDialog.show();
        sginUpImp.onSuccessUpdate(AddCourse.this, name, mID, Token,Level, mLeaderm);
    }
    private void checkSignUpDetails() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(!TextUtils.isEmpty(MName.getText().toString())
                && !TextUtils.isEmpty(mID.getText().toString())
                && !TextUtils.isEmpty(Level.getText().toString())
                && !TextUtils.isEmpty(moduleLeader.getText().toString())){
            //check id if has been used before
            if (courseList.contains(mID.getText().toString())){
                Toast.makeText(AddCourse.this, "The ID has been used before, Please change it", Toast.LENGTH_LONG).show();
            }
            else{
                //send record to be saved
                initSignUp(MName.getText().toString(),
                        mID.getText().toString(),
                        timestamp.toString(),
                        Level.getText().toString(),
                        moduleLeader.getText().toString()); }

        }else{
            if(TextUtils.isEmpty(MName.getText().toString())){
                MName.setError("Name is required");
                return;
            }if(TextUtils.isEmpty(mID.getText().toString())){
                mID.setError("Please enter the ID ");
                return;
            }if (TextUtils.isEmpty(Level.getText().toString())){
                Level.setError("Please enter module level ");
                return;

            }if (TextUtils.isEmpty(moduleLeader.getText().toString())){
                moduleLeader.setError("Please enter module leader");
                return;
            }
        }
    }
    private void checkID(){
        FirebaseFirestore.getInstance().collection("ModuleData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(int i = 0; i < task.getResult().size(); i++){
                        String courseId= task.getResult().getDocuments().get(i).getString("moduleID").toString();
                        courseList.add(courseId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCourse.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onUpdateUpuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(AddCourse.this, "Module has been added", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AddCourse.this, AdminDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(AddCourse.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddCourse.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
