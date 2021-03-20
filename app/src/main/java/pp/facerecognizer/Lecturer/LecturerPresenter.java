package pp.facerecognizer.Lecturer;

import android.app.Activity;

public interface LecturerPresenter {
    void  onSuccessUpdate(Activity activity, String teacherName, String teacherID, String teacherPassword, String teacherEmail, String token);



}
