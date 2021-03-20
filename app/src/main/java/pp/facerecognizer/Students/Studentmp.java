package pp.facerecognizer.Students;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import pp.facerecognizer.Module.ModulePresenter;
import pp.facerecognizer.Module.ModuleView;
import pp.facerecognizer.Module.Modules;

public class Studentmp implements StudentPresenter, StudentView {

    private StudentView studentView;
    private static final String TAG = "StudentImp";

    public Studentmp(StudentView studentView) {
        this.studentView = studentView;
    }

    @Override
    public void onUpdateUpuccess(String message) {
        studentView.onUpdateUpuccess(message);

    }

    @Override
    public void onUpdateFailure(String message) {
        studentView.onUpdateFailure(message);

    }

    @Override
    public void onSuccessUpdate(Activity activity,
                                String StudentName,
                                String StudentID,
                                String Password,
                                String StudentCourse,
                                String Token, String imageUri) {
        StudentModules users = new StudentModules(StudentName, StudentID, Password, StudentCourse, Token , imageUri);
        FirebaseFirestore.getInstance().collection("StudentData").document(Token)
                .set(users, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            studentView.onUpdateUpuccess("Student has been added ");
                        }else {
                            studentView.onUpdateFailure("Something went wrong");
                        }
                    }
                });
    }
}
