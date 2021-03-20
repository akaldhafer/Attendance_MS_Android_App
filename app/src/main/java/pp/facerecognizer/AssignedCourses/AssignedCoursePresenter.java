package pp.facerecognizer.AssignedCourses;

import android.app.Activity;

import java.util.ArrayList;

public interface AssignedCoursePresenter {
    void  onSuccessUpdate(Activity activity, String Intake, String lecturerID, String courseID,
                          String token, ArrayList<String> listOfStudents, String classNo);

}
