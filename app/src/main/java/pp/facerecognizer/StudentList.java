package pp.facerecognizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.StudentListAdapter;
import pp.facerecognizer.Students.StudentModules;
import pp.facerecognizer.Students.StudentView;
import pp.facerecognizer.Students.StudentViewFetch;
import pp.facerecognizer.Students.StudnetImpFetchData;

public class StudentList extends AppCompatActivity implements StudentViewFetch {
    private RecyclerView ListDataView;
    private StudentListAdapter mPostsAdapter;
    ArrayList<StudentModules> jobPostslists = new ArrayList<>();
    private StudnetImpFetchData studnetImpFetchData;
    private static final String TAG = "StudentList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        ListDataView = findViewById(R.id.StudentListView);
        studnetImpFetchData = new StudnetImpFetchData(this,this);
        RecyclerViewMethods();
        studnetImpFetchData.onSuccessUpdate(this);
    }

    private void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new StudentListAdapter(this, jobPostslists);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onUpdateUpuccess(StudentModules message) {
        if(message != null){
            StudentModules studentModules = new StudentModules();
            studentModules.setStudentName(message.getStudentName());
            studentModules.setStudentCourse(message.getStudentCourse());
            studentModules.setStudentID(message.getStudentID());
            jobPostslists.add(studentModules);

            Log.d(TAG, "onUpdateUpuccess: "+message.getStudentCourse());
        }
        mPostsAdapter.notifyDataSetChanged();


    }

    @Override
    public void onUpdateFailure(String message) {

        Toast.makeText(StudentList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentList.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}