package pp.facerecognizer.Students;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class StudnetImpFetchData implements StudentPresenterFetchData {
    private Context context;
    private StudentViewFetch studentView;

    public StudnetImpFetchData(Context context, StudentViewFetch studentView) {
        this.context = context;
        this.studentView = studentView;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("StudentData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String StudentName = task.getResult().getDocuments().get(i).getString("studentName");
                        String studentCourse = task.getResult().getDocuments().get(i).getString("studentCourse");
                        String Password = task.getResult().getDocuments().get(i).getString("Password");
                        String studentID = task.getResult().getDocuments().get(i).getString("studentID");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        String imageUri = task.getResult().getDocuments().get(i).getString("imageUri");
                        StudentModules studentModules = new StudentModules();
                        studentModules.setStudentName(StudentName);
                        studentModules.setStudentCourse(studentCourse);
                        studentModules.setPassword(Password);
                        studentModules.setStudentID(studentID);
                        studentModules.setToken(token);
                        studentModules.setImageUri(imageUri);
                        studentView.onUpdateUpuccess(studentModules);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                studentView.onUpdateFailure(e.getMessage().toString());

            }
        });

    }
}
