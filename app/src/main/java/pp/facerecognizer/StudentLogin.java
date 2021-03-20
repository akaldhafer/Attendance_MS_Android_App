package pp.facerecognizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class StudentLogin extends Activity {
    private EditText UserName, Password;
    FirebaseFirestore firebaseFirestore;
    String name;
    private static final String TAG = "StudentLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        UserName = findViewById(R.id.Studentid);
        Password =  findViewById(R.id.StudentPassword);

        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void StudentLogin(View view) {
        FirebaseAuth.getInstance().signOut();

        String id = UserName.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        if (UserName.getText().equals("")){
            UserName.setError("Student ID is required");
            return;
        }
        if (pass.equals("")){
            Password.setError("Password is required ");
            return;
        }
        // TODO:teacher Login
        firebaseFirestore.collection("StudentData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean CheckUser = false;
                        if (task.isSuccessful()){
                            for (int i=0; i<task.getResult().getDocuments().size(); i++)
                            {
                                String ID = task.getResult().getDocuments().get(i).getString("studentID").trim();
                                String password = task.getResult().getDocuments().get(i).getString("password").trim();
                                String name = task.getResult().getDocuments().get(i).getString("studentName").trim();
                                String course = task.getResult().getDocuments().get(i).getString("studentCourse").trim();
                                String imageuri = task.getResult().getDocuments().get(i).getString("imageUri").trim();

                                if (ID.equals(id) && password.equals(pass)){
                                    Log.d(TAG, "onComplete:  correct ");
                                    Toast.makeText(StudentLogin.this, "Welcome back, "+name,Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(StudentLogin.this, StudentActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("id", ID);
                                    intent.putExtra("course", course);
                                    intent.putExtra("image", imageuri);
                                    startActivity(intent);
                                    finish();
                                    CheckUser = true;
                                }
                            }
                            if (!CheckUser){
                                Toast.makeText(StudentLogin.this, "Wrong ID or Password !",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(StudentLogin.this, "Error, Check Internet connection",Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentLogin.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
