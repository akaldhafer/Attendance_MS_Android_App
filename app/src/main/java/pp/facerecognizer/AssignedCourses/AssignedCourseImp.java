package pp.facerecognizer.AssignedCourses;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignedCourseImp implements AssignedCoursePresenter, AssignedCourseView{

    private AssignedCourseView assignedCourseView;
    public AssignedCourseImp(AssignedCourseView assignedCourseView){
        this.assignedCourseView = assignedCourseView;

    }
    @Override
    public void onSuccessUpdate(Activity activity, String courseIntake, String lecturerName, String courseName,
                                String token, ArrayList<String>ListOfStudents, String classNo) {
        AssignedCourse course = new AssignedCourse(courseIntake, lecturerName, courseName, token, ListOfStudents, classNo);
        FirebaseFirestore.getInstance().collection("AssignedCourse").document(token).set(course, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            assignedCourseView.onUpdateUpuccess("The Course has been assigned and saved");
                        }
                        else {
                            assignedCourseView.onUpdateUpuccess("Something went wrong");
                        }
                    }
                });
    }

    @Override
    public void onUpdateUpuccess(String message) {

        assignedCourseView.onUpdateUpuccess(message);
    }

    @Override
    public void onUpdateFailure(String message) {

        assignedCourseView.onUpdateFailure(message);
    }
}
