package pp.facerecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pp.facerecognizer.login.LoginImp;
import pp.facerecognizer.login.LoginView;

public class AdminLogin extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    Button btnLogin;
    EditText edtEmail, edtPassword;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "AdminLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        edtEmail = findViewById(R.id.EmailAddress);
        edtPassword = findViewById(R.id.Passwords);
        btnLogin = findViewById(R.id.LoginBtn);
        mProgressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog.setMessage("Please wait, Logging in..");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginDetails();
            }
        });

    }

    private void initLogin(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: Login Success");
                    Toast.makeText(AdminLogin.this, "Success Login",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminLogin.this, AdminDashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TAG, "onComplete: F");
                    Toast.makeText(AdminLogin.this, "Username or password is wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkLoginDetails() {
        if(!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())){

            initLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
        }else{
            if(TextUtils.isEmpty(edtEmail.getText().toString())){
                edtEmail.setError("Please enter a valid email");
                return;
            }if(TextUtils.isEmpty(edtPassword.getText().toString())){
                edtPassword.setError("Please enter password");
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminLogin.this, StartScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getUid() != null){
            Intent intent = new Intent(AdminLogin.this, AdminDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

     */
}
