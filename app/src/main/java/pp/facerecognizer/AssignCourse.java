package pp.facerecognizer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.sql.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import fr.ganfra.materialspinner.MaterialSpinner;

import pp.facerecognizer.AssignedCourses.AssignedCourseImp;
import pp.facerecognizer.AssignedCourses.AssignedCourseView;


public class AssignCourse extends AppCompatActivity implements AssignedCourseView {
    public AssignCourse(){

    }
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "AssignCourse";
    String[] item, StudentIDs, TeacherNames;
    private TextView txtSelectedS;
    private EditText edClassNO, edIntake;
    boolean[] checkedItems;
    private Button bselect;
    ArrayList<Integer> userItems = new ArrayList<>();
    private MaterialSpinner courseSpinner, techerSpinner;
    private AssignedCourseImp assignedCourseImp;
    ProgressDialog progressDialog;
    ArrayList<String> studentList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_course);
        firebaseFirestore = FirebaseFirestore.getInstance();

        edClassNO = findViewById(R.id.ClassNo);
        edIntake = findViewById(R.id.Intake);


        courseSpinner = findViewById(R.id.spinnerCourse);
        techerSpinner = findViewById(R.id.spinnerLecturer);

        bselect = findViewById(R.id.bselect);
        txtSelectedS = findViewById(R.id.txtSelectedS);
        assignedCourseImp = new AssignedCourseImp(this);
        bselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AssignCourse.this);
                mBuilder.setTitle("Select Student");
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
                        String items = "";
                        String sID;
                        //to store the selected student id
                        for (int i = 0; i < userItems.size(); i++){
                            sID = StudentIDs[userItems.get(i)];
                            studentList.add(sID);
                        }
                        //to display the selected student id
                        for (int i = 0; i < userItems.size(); i++){
                            items = items + StudentIDs[userItems.get(i)];
                            if (i != userItems.size() -1){
                                items = items+", ";
                            }
                        }
                        txtSelectedS.setText(items);

                        Log.d(TAG, "onClick: items"+items);

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
                                txtSelectedS.setText("No student Selected !");
                            }
                        }
                        else {
                            userItems.clear();
                            txtSelectedS.setText("No student Selected !");
                        }

                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, assigning the course");

    }

    private  void initSignUp(String courseIntake, String lecturerName, String courseName,
                             String token, ArrayList<String>studentIDs, String classNo){
        progressDialog.show();
       assignedCourseImp.onSuccessUpdate(AssignCourse.this, courseIntake, lecturerName, courseName,
               token, studentIDs, classNo);
    }
    // TODO: list all received data into spinner
    void course(){
        firebaseFirestore.collection("ModuleData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            ArrayList<String> listOfCourses = new ArrayList<>();

                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String module =task.getResult().getDocuments().get(i).get("moduleID").toString();
                                listOfCourses.add(module);
                                item = listOfCourses.toArray(new String[listOfCourses.size()]);
                                for(String s : item){
                                    Log.d(TAG, "onComplete: "+s);
                                }

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignCourse.this, android.R.layout.simple_spinner_item, item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            courseSpinner.setAdapter(adapter);

                        }
                    }
                });
    }
    void teacher(){
        firebaseFirestore.collection("TeacherData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            ArrayList<String> listOfCourses = new ArrayList<>();

                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String module =task.getResult().getDocuments().get(i).get("teacherID").toString();
                                listOfCourses.add(module);
                                TeacherNames = listOfCourses.toArray(new String[listOfCourses.size()]);
                                for(String s : TeacherNames){
                                    Log.d(TAG, "onComplete: "+s);
                                }

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignCourse.this, android.R.layout.simple_spinner_item, TeacherNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            techerSpinner.setAdapter(adapter);

                        }
                    }
                });
    }
    void StudentID(){
        firebaseFirestore.collection("StudentData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            ArrayList<String> listOfStudentID = new ArrayList<>();

                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String StudentID =task.getResult().getDocuments().get(i).get("studentID").toString();
                                listOfStudentID.add(StudentID);
                                StudentIDs = listOfStudentID.toArray(new String[listOfStudentID.size()]);
                                for(String s : StudentIDs){
                                    Log.d(TAG, "onComplete: "+s);
                                }

                            }

                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // TODO: call the functions to download the data from the firebase
        StudentID();
         course();
        teacher();
    }
    public void AssignTheCourse(View view) {
        checkSignUpDetails();
    }
    private void checkSignUpDetails() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(!TextUtils.isEmpty(edIntake.getText().toString())
                && !TextUtils.isEmpty(edClassNO.getText().toString())
                && techerSpinner.getSelectedItem() != null
                && courseSpinner.getSelectedItem() != null){
            initSignUp(edIntake.getText().toString(),techerSpinner.getSelectedItem().toString(),
                    courseSpinner.getSelectedItem().toString(),timestamp.toString(),
                    studentList, edClassNO.getText().toString());
        }else{
            if(TextUtils.isEmpty(edIntake.getText().toString())){
                edIntake.setError("Intake is required");
                return;
            }if(TextUtils.isEmpty(edClassNO.getText().toString())){
                edClassNO.setError("Number of classes is required ");
                return;
            }if (techerSpinner.getSelectedItem() == null){
                techerSpinner.setError("Lecturer is required ");
                return;
            }if (courseSpinner.getSelectedItem() == null){
                courseSpinner.setError("Course is required");
                return;
            }
        }
    }
    @Override
    public void onUpdateUpuccess(String message) {
        progressDialog.dismiss();
        Toast.makeText(AssignCourse.this, "Course has been assigned successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AssignCourse.this, AdminDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onUpdateFailure(String message) {
        progressDialog.dismiss();
        Toast.makeText(AssignCourse.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AssignCourse.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
