package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class StudentProfile extends AppCompatActivity {

    private TextView StudentName, StudentID, StudentCourse;
    ImageView studentImage;
    private String sid, sname, scourse, simage;
    private static final String TAG = "StudentProfile";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        StudentName = findViewById(R.id.setStudentNameProfile);
        StudentID = findViewById(R.id.setStudentIDProfile);
        StudentCourse = findViewById(R.id.setStudentCourseProfile);
        studentImage = findViewById(R.id.setStudentImageProfile);

        sid = getIntent().getStringExtra("id");
        sname = getIntent().getStringExtra("name");
        scourse = getIntent().getStringExtra("course");
        simage = getIntent().getStringExtra("image");

        Log.d(TAG, "read name:  "+sname);
        StudentName.setText(sname);
        StudentID.setText(sid);
        StudentCourse.setText(scourse);
        Picasso.with(this).load(simage).fit().into(studentImage);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentProfile.this, StudentActivity.class);
        intent.putExtra("name", sname);
        intent.putExtra("id", sid);
        intent.putExtra("course", scourse);
        intent.putExtra("image", simage);
        startActivity(intent);
        finish();
    }

}
