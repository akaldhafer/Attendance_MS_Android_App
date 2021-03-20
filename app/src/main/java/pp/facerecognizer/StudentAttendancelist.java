package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.StudentAttendanceRecordListAdapter;
import pp.facerecognizer.AttendancePackage.AttendanceRecord;
import pp.facerecognizer.AttendancePackage.AttendanceRecordImpFetchData;
import pp.facerecognizer.AttendancePackage.AttendanceRecordViewFetch;

public class StudentAttendancelist  extends AppCompatActivity implements AttendanceRecordViewFetch {

    private RecyclerView ListDataView;
    private StudentAttendanceRecordListAdapter mPostsAdapter;
    private String lname, lid, simage, scourse;
    ArrayList<AttendanceRecord> jobPostslists = new ArrayList<>();
    private AttendanceRecordImpFetchData attendanceRecordImpFetchData;
    private static final String TAG = "Studentattendancelist";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        ListDataView = findViewById(R.id.AttendanceListView);
        lname = getIntent().getStringExtra("name");
        lid = getIntent().getStringExtra("id");
        scourse = getIntent().getStringExtra("course");
        simage = getIntent().getStringExtra("image");
        attendanceRecordImpFetchData = new AttendanceRecordImpFetchData(this, this);
        RecylerViewMethods();
        attendanceRecordImpFetchData.onSuccessUpdate(this);
    }

    private void RecylerViewMethods() {
        LinearLayoutManager manger = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manger);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new StudentAttendanceRecordListAdapter(this, jobPostslists, lid);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateUpuccess(AttendanceRecord message) {
        if(message != null){
            if (message.getStudentIDs().contains(lid)){
                AttendanceRecord attendanceRecord = new AttendanceRecord();
                attendanceRecord.setIntake(message.getIntake());
                attendanceRecord.setCourseID(message.getCourseID());
                attendanceRecord.setLecturerID(message.getLecturerID());
                attendanceRecord.setNumberOfClasses(message.getNumberOfClasses());
                attendanceRecord.setClassNo(message.getClassNo());
                attendanceRecord.setStudentAttended(message.getStudentAttended());
                attendanceRecord.setStudentIDs(message.getStudentIDs());
                jobPostslists.add(attendanceRecord);
                Log.d(TAG, "onUpdateUpuccess: "+message.getIntake());
            }
        }
        mPostsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(StudentAttendancelist.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentAttendancelist.this, StudentActivity.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", lid);
        intent.putExtra("course", scourse);
        intent.putExtra("image", simage);
        startActivity(intent);
        finish();
    }
}
