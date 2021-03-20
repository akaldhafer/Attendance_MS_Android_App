package pp.facerecognizer.Students;

import android.app.Activity;

public interface StudentPresenter {
    void  onSuccessUpdate(Activity activity, String StudentName,
                          String StudentID,
                          String Password,
                          String StudentCourse,
                          String Token, String imageUri);



}
