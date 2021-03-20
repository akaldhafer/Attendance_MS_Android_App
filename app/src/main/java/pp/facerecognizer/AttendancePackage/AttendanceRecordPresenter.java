package pp.facerecognizer.AttendancePackage;

import android.app.Activity;

import java.util.ArrayList;

public interface AttendanceRecordPresenter {
    void  onSuccessUpdate(Activity activity, String intake, String lecturerID, String courseID,
                          String token, String timeDuration, String numberOfClasses,
                          ArrayList<String> studentIDs, ArrayList<String> studentAttended,
                          int classNo, String available);

}
