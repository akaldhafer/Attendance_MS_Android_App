package pp.facerecognizer.Module;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import pp.facerecognizer.Students.StudentModules;
import pp.facerecognizer.Students.StudentPresenterFetchData;
import pp.facerecognizer.Students.StudentViewFetch;

public class ModuleImpFetchData implements ModulePresenterFetchData {
    private Context context;
    private ModuleViewFetch ModuleView;

    public ModuleImpFetchData(Context context, ModuleViewFetch ModuleView) {
        this.context = context;
        this.ModuleView = ModuleView;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("ModuleData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String CourseName = task.getResult().getDocuments().get(i).getString("moduleName");
                        String CourseID = task.getResult().getDocuments().get(i).getString("moduleID");
                        String Level = task.getResult().getDocuments().get(i).getString("level");
                        String LeaderName = task.getResult().getDocuments().get(i).getString("leader");
                        String token = task.getResult().getDocuments().get(i).getString("deviceToekn");
                        Modules Modules = new Modules();
                        Modules.setModuleName(CourseName);
                        Modules.setModuleID(CourseID);
                        Modules.setLevel(Level);
                        Modules.setLeader(LeaderName);
                        Modules.setDeviceToekn(token);
                        ModuleView.onUpdateUpuccess(Modules);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ModuleView.onUpdateFailure(e.getMessage().toString());

            }
        });

    }
}
