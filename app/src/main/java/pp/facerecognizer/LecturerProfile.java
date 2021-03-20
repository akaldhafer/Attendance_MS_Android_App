package pp.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LecturerProfile extends AppCompatActivity {
    private TextView lecturerName, LecturerID, LecturerEmail;
    private String lname, lid, lemail;
    private static final String TAG = "LecturerProfile";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_profile);

        lecturerName = findViewById(R.id.setlecturerNameProfile);
        LecturerID = findViewById(R.id.setlecturerIDProfile);
        LecturerEmail = findViewById(R.id.setlecturerEmailProfile);

        lname = getIntent().getStringExtra("name");
        lid = getIntent().getStringExtra("id");
        lemail = getIntent().getStringExtra("email");

        Log.d(TAG, "read name:  "+lname);
        lecturerName.setText(lname);
        LecturerID.setText(lid);
        LecturerEmail.setText(lemail);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LecturerProfile.this, TeachersActivity.class);
        intent.putExtra("name", lname);
        intent.putExtra("id", lid);
        intent.putExtra("email",lemail);
        startActivity(intent);
        finish();
    }
}
