package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.LecturerListAdapter;
import pp.facerecognizer.Lecturer.LecturerModules;
import pp.facerecognizer.Lecturer.LecturermpFetchData;
import pp.facerecognizer.Lecturer.LecturerViewFetch;


public class lecturerList extends AppCompatActivity implements LecturerViewFetch {
    private RecyclerView ListDataView;
    private LecturerListAdapter mPostsAdapter;
    ArrayList<LecturerModules> jobPostslists = new ArrayList<>();
    private LecturermpFetchData lecturermpFetchData;
    private static final String TAG = "LecturerList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_list);
        ListDataView = findViewById(R.id.LecturerListView);
        lecturermpFetchData = new LecturermpFetchData(this,this);
        RecyclerViewMethods();
        lecturermpFetchData.onSuccessUpdate(this);
    }

    private void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new LecturerListAdapter(this, jobPostslists);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onUpdateUpuccess(LecturerModules message) {
        if(message != null){
            LecturerModules lecturerModules = new LecturerModules();
            lecturerModules.setTeacherName(message.getTeacherName());
            lecturerModules.setTeacherEmail(message.getTeacherEmail());
            lecturerModules.setTeacherID(message.getTeacherID());
            jobPostslists.add(lecturerModules);

            Log.d(TAG, "onUpdateUpuccess: "+message.getTeacherName());
        }
        mPostsAdapter.notifyDataSetChanged();


    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(lecturerList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(lecturerList.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
