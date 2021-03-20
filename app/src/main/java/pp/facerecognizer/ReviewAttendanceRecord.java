package pp.facerecognizer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class ReviewAttendanceRecord extends AppCompatActivity {
    private static final String TAG = "ReviewAttendance";
    private FirebaseFirestore firebaseFirestore;

    boolean[] checkedItems;
    private Button refresh, saveRecord;
    private TextView edLecturerID, edCourseID, edClassNo, edIntake;
    ProgressDialog progressDialog;
    ArrayList<Integer> userItems = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    ArrayList<String> manualStudentList = new ArrayList<>();
    ArrayList<String> AttendedStudentList = new ArrayList<>();
    String[]  StudentIDs, AttendedStudent;
    private TextView mylistAttendStudent;

    private String lname, lecturerID, lemail, time, courseID, Intake,classNo, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_attendance);
        firebaseFirestore = FirebaseFirestore.getInstance();


        //assign the details
        saveRecord = findViewById(R.id.StopRecordingAndExit);
        refresh = findViewById(R.id.ViewAttendanceRecord);
        mylistAttendStudent = findViewById(R.id.ListofAttendStudent);
        edClassNo = findViewById(R.id.attendedClassNo);
        edCourseID = findViewById(R.id.attendedCourseID);
        edLecturerID = findViewById(R.id.attendedLecturerID);
        edIntake = findViewById(R.id.attendedIntake);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, attendance is recording..");
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAttendedList();
            }
        });
        saveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndExit();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: call the functions to download the data from the firebase
        //get the details
        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");
        lname = getIntent().getStringExtra("name");
        lemail = getIntent().getStringExtra("email");
        GetUpdatedAttendance();
    }
    private void GetUpdatedAttendance() {

        DocumentReference user = firebaseFirestore.collection("AttendanceRecords").document(time);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    lecturerID = doc.get("lecturerID").toString();
                    courseID = doc.get("courseID").toString();
                    Intake = doc.get("intake").toString();
                    classNo = doc.get("classNo").toString();
                    //set the details
                    edIntake.setText(Intake);
                    edLecturerID.setText(lecturerID);
                    edCourseID.setText(courseID);
                    edClassNo.setText(classNo);
                    //get the student list
                    ArrayList<String> studentID = (ArrayList<String>) doc.get("studentIDs");
                    studentList.clear();
                    studentList.addAll(studentID);
                    StudentIDs = null;
                    StudentIDs = studentList.toArray(new String[studentList.size()]);
                    //get attended student
                    ArrayList<String> attendedstudent = (ArrayList<String>) doc.get("studentAttended");
                    AttendedStudentList.clear();
                    AttendedStudentList.addAll(attendedstudent);
                    AttendedStudent = null;
                    AttendedStudent = AttendedStudentList.toArray(new String[AttendedStudentList.size()]);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewAttendanceRecord.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    private void ViewAttendedList() {
        String items= "";
        GetUpdatedAttendance();
        if (AttendedStudent != null){
            for (int i = 0 ; i < AttendedStudent.length; i++){
                for (int j=0; j<StudentIDs.length; j++){
                    if (AttendedStudent[i].equals(StudentIDs[j])){
                        items = items + StudentIDs[j]+"\n";
                        Log.d(TAG, "ViewAttendanceRecord: "+AttendedStudent[i]);
                    }
                }
            }
        }
        //to display the selected student id
        mylistAttendStudent.setText(items);

    }
    public void RecordManually(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ReviewAttendanceRecord.this);
        mBuilder.setTitle("Mark Attendance Manually");
        mBuilder.setMultiChoiceItems(StudentIDs, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    if(! userItems.contains(which)){
                        userItems.add(which);
                        Log.d(TAG, "onClick: ");
                    }
                    else{
                        userItems.remove(which);
                    }
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sID;
                //to store the selected student id
                for (int i = 0; i < userItems.size(); i++){
                    sID = StudentIDs[userItems.get(i)];
                    if (!manualStudentList.contains(sID)){
                        manualStudentList.add(sID);
                    }
                }
            }
        });
        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(checkedItems != null){
                    for (int i=0; i<checkedItems.length; i++){
                        checkedItems[i] = false;
                        userItems.clear();
                    }
                }
                else {
                    userItems.clear();
                }

            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    private void SaveAndExit() {
        //Merge two arraylists without duplicates
        List<String> CurrentAttendedStudent = new ArrayList<>(manualStudentList);
        CurrentAttendedStudent.removeAll(AttendedStudentList);
        AttendedStudentList.addAll(CurrentAttendedStudent);


        String available = "false";
        DocumentReference record = firebaseFirestore.collection("AttendanceRecords").document(time);
        record.update("studentAttended", AttendedStudentList);
        record.update("available", available)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ReviewAttendanceRecord.this, "Attendance Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReviewAttendanceRecord.this, TeachersActivity.class);
                        intent.putExtra("name", lname);
                        intent.putExtra("id", id);
                        intent.putExtra("email",lemail);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewAttendanceRecord.this, "Something went wrong, check the internet connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SaveAndExit();
        Intent intent = new Intent(ReviewAttendanceRecord.this, TeachersActivity.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", id);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }


}
