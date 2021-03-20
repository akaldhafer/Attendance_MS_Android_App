package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    private static final String TAG = "StudentActivity";
    private TextView setStudentName;
    ImageView imageView;
    boolean found = false, AlreadyAttended = false;

    private String timestamp;
    private String sid, sname, scourse, simage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_actvity);
        sid = getIntent().getStringExtra("id");
        sname = getIntent().getStringExtra("name");
        scourse = getIntent().getStringExtra("course");
        simage = getIntent().getStringExtra("image");

        Log.d(TAG, "onCreate: "+simage);
        setStudentName = findViewById(R.id.setStudentName);
        imageView = findViewById(R.id.setStudentPhoto);
        //set the student image
        Picasso.with(this).load(simage).resize(120,120).into(imageView);
        setStudentName.setText("Hi!, "+sname);
        CheckAvailableAttendance();


    }

    public void CheckAvailableAttendance(){
        FirebaseFirestore.getInstance().collection("AttendanceRecords")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(int i = 0; i < task.getResult().size(); i++){
                        ArrayList<String> listOfStudentID = new ArrayList<>();
                        ArrayList<String> listOfStudentAttended = new ArrayList<>();
                        String Available= task.getResult().getDocuments().get(i).getString("available").toString();
                        if(Available.equals("true")){//check if the attendance is active
                            ArrayList<String> studentid = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentIDs");
                            ArrayList<String> attendedStudent = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentAttended");
                            listOfStudentID.addAll(studentid);
                            listOfStudentAttended.addAll(attendedStudent);
                            if (listOfStudentAttended.contains(sid)){
                                //store students IDs
                                timestamp = task.getResult().getDocuments().get(i).getString("token").toString();
                                AlreadyAttended = true;
                            }
                            if (listOfStudentID.contains(sid)){
                                //store students IDs
                                timestamp = task.getResult().getDocuments().get(i).getString("token").toString();
                                found = true;
                            }

                        }

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void TakeAttendance(View view) {
        if(found){
            if (AlreadyAttended){
                Toast.makeText(StudentActivity.this, "You have already take the attendance", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentActivity.this, StudentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", sname);
                intent.putExtra("id", sid);
                intent.putExtra("course", scourse);
                intent.putExtra("image", simage);
                intent.putExtra("time", timestamp);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(StudentActivity.this, "Please verify your identity to record the attendance", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", sname);
                intent.putExtra("id", sid);
                intent.putExtra("course", scourse);
                intent.putExtra("image", simage);
                intent.putExtra("time", timestamp);
                startActivity(intent);
                finish();
            }
        }
        else{
            Toast.makeText(StudentActivity.this, "No active attendance record found!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(StudentActivity.this, StudentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("name", sname);
            intent.putExtra("id", sid);
            intent.putExtra("course", scourse);
            intent.putExtra("image", simage);
            intent.putExtra("time", timestamp);
            startActivity(intent);
            finish();
        }
    }

    public void ListAttendance(View view) {
        Intent intent = new Intent(StudentActivity.this, StudentAttendancelist.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", sname);
        intent.putExtra("id", sid);
        intent.putExtra("course", scourse);
        intent.putExtra("image", simage);
        startActivity(intent);
        finish();
    }

    public void StudentProfile(View view) {
        Intent intent = new Intent(StudentActivity.this, StudentProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", sname);
        intent.putExtra("id", sid);
        intent.putExtra("course", scourse);
        intent.putExtra("image", simage);
        startActivity(intent);
        finish();
    }

    public void StudentLogOut(View view) {
        Intent intent = new Intent(StudentActivity.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentActivity.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
