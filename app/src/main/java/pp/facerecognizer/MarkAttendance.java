package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import pp.facerecognizer.AttendancePackage.AttendanceRecordImp;
import pp.facerecognizer.AttendancePackage.AttendanceRecordView;

public class MarkAttendance extends AppCompatActivity implements AttendanceRecordView {

    private static final String TAG = "MarkAttendance";
    private FirebaseFirestore firebaseFirestore;
    String available;
    private int CurrentClassNo = 0;
    private EditText recordClassNo;
    private MaterialSpinner courseSpinner, intakeSpinner, timeDurationSpinner;
    private AttendanceRecordImp attendanceRecordImp;
    ProgressDialog progressDialog;
    public ArrayList<String> studentList = new ArrayList<>();
    public ArrayList<String> studentAttendedList = new ArrayList<>();
    String[] CourseIDitem, Intakeitem, StudentIDs;
    String[] TimeDurationitem ={"60 min", "120 min"};
    private String lname, CurrentLecturerID, lemail, time, currentNoOfClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_attendance);
        firebaseFirestore = FirebaseFirestore.getInstance();
        attendanceRecordImp = new AttendanceRecordImp(this);

        recordClassNo = findViewById(R.id.RecordClassNo);
        courseSpinner = findViewById(R.id.spinnerCourseAttendance);
        intakeSpinner = findViewById(R.id.spinnerIntake);
        timeDurationSpinner =findViewById(R.id.spinnerTimeDuration);

        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(MarkAttendance.this, android.R.layout.simple_spinner_item, TimeDurationitem);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeDurationSpinner.setAdapter(adapterTime);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, attendance is recording..");
    }

    @Override
    protected void onStart() {
        super.onStart();
        lname = getIntent().getStringExtra("name");
        CurrentLecturerID = getIntent().getStringExtra("id");
        lemail = getIntent().getStringExtra("email");
        // TODO: call the functions to download the data from the firebase
        assignedcourse();

    }
    void assignedcourse(){
        firebaseFirestore.collection("AssignedCourse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            ArrayList<String> listOfCourseID = new ArrayList<>();
                            ArrayList<String> listOfIntake = new ArrayList<>();
                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String lecturerID =task.getResult().getDocuments().get(i).get("lecturerID").toString();
                                if(lecturerID.equals(CurrentLecturerID)){
                                    String courseID =task.getResult().getDocuments().get(i).get("courseID").toString();
                                    String intake =task.getResult().getDocuments().get(i).get("intake").toString();

                                    listOfIntake.add(intake);
                                    listOfCourseID.add(courseID);

                                    CourseIDitem = listOfCourseID.toArray(new String[listOfCourseID.size()]);
                                    Intakeitem = listOfIntake.toArray(new String[listOfIntake.size()]);

                                    for(String s : Intakeitem){
                                        Log.d(TAG, "onComplete: "+s);
                                    }
                                }

                            }
                            ArrayAdapter<String> adapterCourseID = new
                                    ArrayAdapter<String>(MarkAttendance.this, android.R.layout.simple_spinner_item, CourseIDitem);
                            ArrayAdapter<String> adapterIntake = new
                                    ArrayAdapter<String>(MarkAttendance.this, android.R.layout.simple_spinner_item, Intakeitem);
                            adapterCourseID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            adapterIntake.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            courseSpinner.setAdapter(adapterCourseID);
                            intakeSpinner.setAdapter(adapterIntake);

                        }
                    }
                });
    }
    void getStudentIDs(){
        firebaseFirestore.collection("AssignedCourse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                String lecturerID = task.getResult().getDocuments().get(i).get("lecturerID").toString();
                                String courseID = task.getResult().getDocuments().get(i).get("courseID").toString();
                                String intake = task.getResult().getDocuments().get(i).get("intake").toString();
                                if (lecturerID.equals(CurrentLecturerID) &&
                                        courseID.equals(courseSpinner.getSelectedItem().toString()) &&
                                        intake.equals(intakeSpinner.getSelectedItem().toString())) {

                                    currentNoOfClasses = task.getResult().getDocuments().get(i).get("classNo").toString();
                                    Log.d(TAG, "onComplete: currentNoOfClasses" + currentNoOfClasses);
                                    ArrayList<String> studentid = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentIDs");
                                    studentList.addAll(studentid);
                                    StudentIDs = studentList.toArray(new String[studentList.size()]);
                                    for (String s : StudentIDs) {
                                        Log.d(TAG, "onComplete: " + s);
                                    }
                                    checkSignUpDetails();
                                    break;
                                }

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MarkAttendance.this, "Student List could not found!", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Toast.makeText(MarkAttendance.this, "Student List is ready!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void initSignUp(String intake, String lecturerID, String courseID,
                             String token, String timeDuration, String numberOfClasses,
                             ArrayList<String> studentIDs, ArrayList<String> studentAttended,
                             int classNo, String Available){
        progressDialog.show();
        attendanceRecordImp.onSuccessUpdate(MarkAttendance.this,  intake,  lecturerID, courseID,
                 token,  timeDuration,  numberOfClasses,
                 studentIDs,studentAttended,  classNo,  Available);

    }
    public void StartRecording(View view) {
        getStudentIDs();
    }
    private void checkSignUpDetails() {
        available = "true";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.toString();
        if(timeDurationSpinner.getSelectedItem()!= null
                && !TextUtils.isEmpty(recordClassNo.getText().toString())
                && timeDurationSpinner != null
                && intakeSpinner.getSelectedItem() != null
                && intakeSpinner != null
                && courseSpinner.getSelectedItem() != null
                && courseSpinner != null)
        {
            CurrentClassNo = Integer.parseInt(recordClassNo.getText().toString());
            progressDialog.show();
            initSignUp(intakeSpinner.getSelectedItem().toString(),
                    CurrentLecturerID, courseSpinner.getSelectedItem().toString(),
                    time,
                    timeDurationSpinner.getSelectedItem().toString(),
                    currentNoOfClasses,
                    studentList,
                    studentAttendedList,
                    CurrentClassNo,
                    available);
        }else{
            if(timeDurationSpinner.getSelectedItem()== null
                    || timeDurationSpinner == null){
                timeDurationSpinner.setError("Time Duration is required!");
                return;
            }
            if (intakeSpinner.getSelectedItem() == null
                    || intakeSpinner == null){
                intakeSpinner.setError("Intake is required!");
                return;
            }if (courseSpinner.getSelectedItem() == null
                    || courseSpinner == null){
                courseSpinner.setError("Course ID is required!");
                return;
            }
            if (TextUtils.isEmpty(recordClassNo.getText().toString())){
                recordClassNo.setError("Class No is required!");
                return;
            }
        }
    }

    @Override
    public void onUpdateUpuccess(String message) {
        progressDialog.dismiss();
        Toast.makeText(MarkAttendance.this, "Attendance has been started", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MarkAttendance.this, ReviewAttendanceRecord.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", CurrentLecturerID);
        intent.putExtra("email",lemail);
        intent.putExtra("time",time);
        startActivity(intent);
        finish();
    }
    @Override
    public void onUpdateFailure(String message) {
       progressDialog.dismiss();
       Toast.makeText(MarkAttendance.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MarkAttendance.this, TeachersActivity.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", CurrentLecturerID);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }

}
