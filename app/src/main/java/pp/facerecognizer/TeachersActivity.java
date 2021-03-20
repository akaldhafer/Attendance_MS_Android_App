package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TeachersActivity extends AppCompatActivity {
    private static final String TAG = "TeachersActivity";
    TextView setLecturerName;
    boolean found = false;
    private String lname, lid, lemail;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_actvity);
        setLecturerName = findViewById(R.id.setlecturerName);
        firebaseFirestore = FirebaseFirestore.getInstance();
        lname = getIntent().getStringExtra("name");
        lid = getIntent().getStringExtra("id");
        lemail = getIntent().getStringExtra("email");

        Log.d(TAG, "onCreate: "+lname);
        setLecturerName.setText("Welcome back, "+lname);
        checkavaiableCourse();

    }

    void checkavaiableCourse(){
        firebaseFirestore.collection("AssignedCourse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String lecturerID =task.getResult().getDocuments().get(i).get("lecturerID").toString();
                                if(lecturerID.equals(lid)){
                                    found = true;
                                }

                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeachersActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void TakeAttendance(View view) {
        if (found){
            Intent intent = new Intent(TeachersActivity.this, MarkAttendance.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("name", lname);
            intent.putExtra("id", lid);
            intent.putExtra("email",lemail);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(TeachersActivity.this, "No assigned course found!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TeachersActivity.this, TeachersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("name", lname);
            intent.putExtra("id", lid);
            intent.putExtra("email",lemail);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TeachersActivity.this, TeacherLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void LecturerProfile(View view) {
        Intent intent = new Intent(TeachersActivity.this, LecturerProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", lname);
        intent.putExtra("id", lid);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }

    public void ListAttendance(View view) {
        Intent intent = new Intent(TeachersActivity.this, AttendanceList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", lname);
        intent.putExtra("id", lid);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }

    public void LecturerLogOut(View view) {
        Intent intent = new Intent(TeachersActivity.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
