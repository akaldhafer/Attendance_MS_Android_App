package pp.facerecognizer.Lecturer;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Lecturermp implements LecturerPresenter, LecturerView {

    private LecturerView teachermp;
    private static final String TAG = "LecturerImp";

    public Lecturermp(LecturerView teachermp) {
        this.teachermp = teachermp;
    }

    @Override
    public void onUpdateUpuccess(String message) {
        teachermp.onUpdateUpuccess(message);
    }

    @Override
    public void onUpdateFailure(String message) {
        teachermp.onUpdateFailure(message);
    }

    @Override
    public void onSuccessUpdate(Activity activity,
                                String TeacherName ,
                                String TeacherID,
                                String TeacherPassword,
                                String TeacherEmail,
                                String Token) {
        LecturerModules users = new LecturerModules( TeacherName ,TeacherID, TeacherPassword, TeacherEmail, Token);
        FirebaseFirestore.getInstance().collection("TeacherData").document(Token)
                .set(users, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            teachermp.onUpdateUpuccess("Teacher record has been added");

                        }else {
                            teachermp.onUpdateFailure("Something went wrong");
                        }
                    }
                });
    }
}
