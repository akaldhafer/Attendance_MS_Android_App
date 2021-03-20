package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TeacherLogin extends AppCompatActivity {
    private EditText UserName, Password;
    FirebaseFirestore firebaseFirestore;
    private static final String TAG = "TeacherLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        UserName = findViewById(R.id.UserId);
        Password = findViewById(R.id.input_password);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void LoginTeacher(View view) {
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG, "LoginTeacher: ");
        String id = UserName.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        if (UserName.getText().equals("")){
            UserName.setError("Lecturer ID is required");
            return;
        }
        if (pass.equals("")){
            Password.setError("Password is required ");
            return;
        }
        // TODO:teacher Login
        firebaseFirestore.collection("TeacherData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean CheckUser = false;
                        if (task.isSuccessful()){
                            for (int i=0; i<task.getResult().getDocuments().size(); i++)
                            {
                                String ID = task.getResult().getDocuments().get(i).getString("teacherID").trim();
                                String password = task.getResult().getDocuments().get(i).getString("teacherPassword").trim();
                                String name = task.getResult().getDocuments().get(i).getString("teacherName").trim();
                                String email = task.getResult().getDocuments().get(i).getString("teacherEmail").trim();
                                if (ID.equals(id) && password.equals(pass)){//check id and password
                                    Log.d(TAG, "onComplete:  correct ");
                                    Toast.makeText(TeacherLogin.this, "Welcome back, "+name,Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "current user:  "+name);
                                    Intent intent = new Intent(TeacherLogin.this, TeachersActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("id", ID);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                    finish();
                                    CheckUser = true;
                                }
                            }
                            if (!CheckUser){
                                Toast.makeText(TeacherLogin.this, "Wrong ID or Password !",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(TeacherLogin.this, "Error, Check Internet connection",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TeacherLogin.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
