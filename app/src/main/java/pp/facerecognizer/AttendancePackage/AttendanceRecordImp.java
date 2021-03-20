package pp.facerecognizer.AttendancePackage;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import pp.facerecognizer.AssignedCourses.AssignedCourse;
import pp.facerecognizer.AssignedCourses.AssignedCoursePresenter;
import pp.facerecognizer.AssignedCourses.AssignedCourseView;

public class AttendanceRecordImp implements AttendanceRecordPresenter, AttendanceRecordView {

    private AttendanceRecordView attendanceRecordView;
    public AttendanceRecordImp(AttendanceRecordView attendanceRecordView){
        this.attendanceRecordView = attendanceRecordView;
    }
    @Override
    public void onSuccessUpdate(Activity activity, String intake, String lecturerID, String courseID,
                                String token, String timeDuration, String numberOfClasses,
                                ArrayList<String> studentIDs, ArrayList<String> studentAttended,
                                int classNo, String available) {
        AttendanceRecord course = new AttendanceRecord( intake,  lecturerID,  courseID,
                 token,  timeDuration,  numberOfClasses,
               studentIDs, studentAttended,  classNo,  available);
        FirebaseFirestore.getInstance().collection("AttendanceRecords").document(token).set(course, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            attendanceRecordView.onUpdateUpuccess("Recording the Attendance Started");
                        }
                        else {
                            attendanceRecordView.onUpdateUpuccess("Something went wrong");
                        }
                    }
                });
    }

    @Override
    public void onUpdateUpuccess(String message) {

        attendanceRecordView.onUpdateUpuccess(message);
    }

    @Override
    public void onUpdateFailure(String message) {

        attendanceRecordView.onUpdateFailure(message);
    }
}
