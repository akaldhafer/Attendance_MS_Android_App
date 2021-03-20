package pp.facerecognizer.Module;

import android.app.Activity;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class ModuleImp implements ModulePresenter, ModuleView {

    private ModuleView moduleView;
    private static final String TAG = "ModuleImp";

    public ModuleImp(ModuleView moduleView) {
        this.moduleView = moduleView;
    }

    @Override
    public void onUpdateUpuccess(String message) {
       moduleView.onUpdateUpuccess(message);

    }

    @Override
    public void onUpdateFailure(String message) {
        moduleView.onUpdateFailure(message);
    }

    @Override
    public void onSuccessUpdate(Activity activity,
                                String ModuleName,
                                String ModuleID,
                                String DeviceToekn,
                                String Level,
                                String Leader) {
        Modules users = new Modules(ModuleName, ModuleID, DeviceToekn, Level, Leader );
        FirebaseFirestore.getInstance().collection("ModuleData").document(DeviceToekn)
                .set(users, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            moduleView.onUpdateUpuccess("Course has been added ");

                        }else {
                            moduleView.onUpdateFailure("Something went wrong");
                        }
                    }
                });
    }
}
