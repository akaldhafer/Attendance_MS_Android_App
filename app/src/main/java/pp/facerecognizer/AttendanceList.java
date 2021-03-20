package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.AttendanceRecordListAdapter;
import pp.facerecognizer.AttendancePackage.AttendanceRecord;
import pp.facerecognizer.AttendancePackage.AttendanceRecordImpFetchData;
import pp.facerecognizer.AttendancePackage.AttendanceRecordViewFetch;

public class AttendanceList extends AppCompatActivity implements AttendanceRecordViewFetch {
    private RecyclerView ListDataView;
    private AttendanceRecordListAdapter mPostsAdapter;
    private String lname, lid, lemail;
    ArrayList<AttendanceRecord> jobPostslists = new ArrayList<>();
    private AttendanceRecordImpFetchData attendanceRecordImpFetchData;
    private static final String TAG = "attendancelist";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        ListDataView = findViewById(R.id.AttendanceListView);
        lname = getIntent().getStringExtra("name");
        lid = getIntent().getStringExtra("id");
        lemail = getIntent().getStringExtra("email");
        attendanceRecordImpFetchData = new AttendanceRecordImpFetchData(this, this);
        RecylerViewMethods();
        attendanceRecordImpFetchData.onSuccessUpdate(this);
    }

    private void RecylerViewMethods() {
        LinearLayoutManager manger = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manger);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new AttendanceRecordListAdapter(this, jobPostslists);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateUpuccess(AttendanceRecord message) {
        if(message != null){
            if (message.getLecturerID().equals(lid)){
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
        Toast.makeText(AttendanceList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AttendanceList.this, TeachersActivity.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", lid);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }
}
