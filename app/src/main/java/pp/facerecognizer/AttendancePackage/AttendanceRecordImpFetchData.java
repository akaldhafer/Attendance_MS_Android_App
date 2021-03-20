package pp.facerecognizer.AttendancePackage;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import pp.facerecognizer.AssignedCourses.AssignedCourse;
import pp.facerecognizer.AssignedCourses.AssignedCoursePresenterFetchData;
import pp.facerecognizer.AssignedCourses.AssignedCourseViewFetch;


public class AttendanceRecordImpFetchData implements AttendanceRecordPresenterFetchData {
    private Context context;
    private AttendanceRecordViewFetch attendanceRecordViewFetch;

    public AttendanceRecordImpFetchData(Context context, AttendanceRecordViewFetch attendanceRecordViewFetch){
        this.context = context;
        this.attendanceRecordViewFetch = attendanceRecordViewFetch;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {

        FirebaseFirestore.getInstance().collection("AttendanceRecords")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(int i = 0; i < task.getResult().size(); i++){
                        //fetch data
                        String courseIntake = task.getResult().getDocuments().get(i).getString("intake");
                        String LecturerID= task.getResult().getDocuments().get(i).getString("lecturerID");
                        ArrayList<String> studentid = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentIDs");
                        ArrayList<String> studentAttended = (ArrayList<String>) task.getResult().getDocuments().get(i).get("studentAttended");
                        String CourseID= task.getResult().getDocuments().get(i).getString("courseID");
                        String TimeDuration= task.getResult().getDocuments().get(i).getString("timeDuration");
                        String NoOfClass= task.getResult().getDocuments().get(i).getString("numberOfClasses");
                        int ClassNo= Integer.parseInt(task.getResult().getDocuments().get(i).get("classNo").toString());
                        String Available= task.getResult().getDocuments().get(i).getString("available");
                        //set data
                        AttendanceRecord attendanceRecord = new AttendanceRecord();
                        attendanceRecord.setAvailable(Available);
                        attendanceRecord.setClassNo(ClassNo);
                        attendanceRecord.setNumberOfClasses(NoOfClass);
                        attendanceRecord.setIntake(courseIntake);
                        attendanceRecord.setLecturerID(LecturerID);
                        attendanceRecord.setCourseID(CourseID);
                        attendanceRecord.setStudentIDs(studentid);
                        attendanceRecord.setStudentAttended(studentAttended);
                        attendanceRecord.setTimeDuration(TimeDuration);
                        attendanceRecordViewFetch.onUpdateUpuccess(attendanceRecord);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                attendanceRecordViewFetch.onUpdateFailure(e.getMessage().toString());
            }
        });
    }
}
