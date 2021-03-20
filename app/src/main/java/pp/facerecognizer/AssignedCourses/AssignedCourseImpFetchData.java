package pp.facerecognizer.AssignedCourses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AssignedCourseImpFetchData implements AssignedCoursePresenterFetchData{
    private Context context;
    private AssignedCourseViewFetch assignedCourseViewFetch;
    private static final String TAG = "AssignedCourseImpFetchD";

    public AssignedCourseImpFetchData(Context context, AssignedCourseViewFetch assignedCourseViewFetch){
        this.context = context;
        this.assignedCourseViewFetch = assignedCourseViewFetch;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {

        FirebaseFirestore.getInstance().collection("AssignedCourse")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for(int i = 0; i < task.getResult().size(); i++){
                        String courseIntake = task.getResult().getDocuments().get(i).getString("intake");
                        String LecturerID= task.getResult().getDocuments().get(i).getString("lecturerID");
                        ArrayList<String> studentid = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentIDs");
                        Log.d(TAG, "onComplete: "+studentid.get(0));

                        String CourseID= task.getResult().getDocuments().get(i).getString("courseID");
                        String ClassN= task.getResult().getDocuments().get(i).getString("classNo");

                        AssignedCourse assignedCourse = new AssignedCourse();
                        assignedCourse.setIntake(courseIntake);
                        assignedCourse.setLecturerID(LecturerID);
                        assignedCourse.setCourseID(CourseID);
                        assignedCourse.setStudentIDs(studentid);
                        assignedCourse.setClassNo(ClassN);
                        assignedCourseViewFetch.onUpdateUpuccess(assignedCourse);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assignedCourseViewFetch.onUpdateFailure(e.getMessage().toString());
            }
        });
    }
}
