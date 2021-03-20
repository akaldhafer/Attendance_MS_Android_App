package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Adapter.CourseListAdapter;
import pp.facerecognizer.Module.ModuleImpFetchData;
import pp.facerecognizer.Module.ModuleViewFetch;
import pp.facerecognizer.Module.Modules;


public class CourseList extends AppCompatActivity implements ModuleViewFetch {
    private RecyclerView ListDataView;
    private CourseListAdapter mPostsAdapter;
    ArrayList<Modules> jobPostslists = new ArrayList<>();
    private ModuleImpFetchData moduleImpFetchData;
    private static final String TAG = "CourseList";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        ListDataView = findViewById(R.id.CourseListView);
        moduleImpFetchData = new ModuleImpFetchData(this, this);

        RecylerViewMethods();
        moduleImpFetchData.onSuccessUpdate(this);
    }

    private void RecylerViewMethods() {
        LinearLayoutManager manger = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manger);
        ListDataView.setHasFixedSize(true);
        mPostsAdapter = new CourseListAdapter(this, jobPostslists);
        ListDataView.setAdapter(mPostsAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateUpuccess(Modules message) {
        if(message != null){
            Modules modules = new Modules();
            modules.setModuleName(message.getModuleName());
            modules.setModuleID(message.getModuleID());
            modules.setLeader(message.getLeader());
            modules.setLevel(message.getLevel());
            jobPostslists.add(modules);
            Log.d(TAG, "onUpdateUpuccess: "+message.getModuleName());
        }
        mPostsAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onUpdateFailure(String message) {

        Toast.makeText(CourseList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CourseList.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }
}
