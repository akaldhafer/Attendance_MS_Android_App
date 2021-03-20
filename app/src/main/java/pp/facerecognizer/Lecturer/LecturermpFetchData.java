package pp.facerecognizer.Lecturer;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class LecturermpFetchData implements LecturerPresenterFetchData {


    private Context context;
    private LecturerViewFetch teacherView;
    public LecturermpFetchData(Context context, LecturerViewFetch teacherView) {
        this.context = context;
        this.teacherView = teacherView;
    }
    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("TeacherData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String LecturerName = task.getResult().getDocuments().get(i).getString("teacherName");
                        String LecturerEmail = task.getResult().getDocuments().get(i).getString("teacherEmail");
                        String LecturerID = task.getResult().getDocuments().get(i).getString("teacherID");

                        LecturerModules lecturerModules = new LecturerModules();
                        lecturerModules.setTeacherName(LecturerName);
                        lecturerModules.setTeacherEmail(LecturerEmail);
                        lecturerModules.setTeacherID(LecturerID);
                        teacherView.onUpdateUpuccess(lecturerModules);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                teacherView.onUpdateFailure(e.getMessage().toString());

            }
        });


    }


}
