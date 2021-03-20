package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.AssignedCourseListAdapter;
import pp.facerecognizer.Adapter.CourseListAdapter;
import pp.facerecognizer.AssignedCourses.AssignedCourse;
import pp.facerecognizer.AssignedCourses.AssignedCourseImpFetchData;
import pp.facerecognizer.AssignedCourses.AssignedCourseViewFetch;


public class AssignedCourseList extends AppCompatActivity implements AssignedCourseViewFetch {
    private RecyclerView ListDataView;
    private AssignedCourseListAdapter mPostsAdapter;
    ArrayList<AssignedCourse> jobPostslists = new ArrayList<>();
    private AssignedCourseImpFetchData moduleImpFetchData;
    private static final String TAG = "AssignedCourseList";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignedcourse_list);
        ListDataView = findViewById(R.id.AssignedCourseListView);
        moduleImpFetchData = new AssignedCourseImpFetchData(this, this);

        RecylerViewMethods();
        moduleImpFetchData.onSuccessUpdate(this);
    }

    private void RecylerViewMethods() {
        LinearLayoutManager manger = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manger);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new AssignedCourseListAdapter(this, jobPostslists);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateUpuccess(AssignedCourse message) {
        if(message != null){
            AssignedCourse modules = new AssignedCourse();
            modules.setIntake(message.getIntake());
            modules.setClassNo(message.getClassNo());
            modules.setCourseID(message.getCourseID());
            modules.setLecturerID(message.getLecturerID());
            modules.setStudentIDs(message.getStudentIDs());
            jobPostslists.add(modules);
            Log.d(TAG, "onUpdateUpuccess: "+message.getIntake());
        }
        mPostsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onUpdateFailure(String message) {

        Toast.makeText(AssignedCourseList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AssignedCourseList.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
