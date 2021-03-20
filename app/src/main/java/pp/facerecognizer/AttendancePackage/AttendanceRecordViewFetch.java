package pp.facerecognizer.AttendancePackage;


public interface AttendanceRecordViewFetch {

    void onUpdateUpuccess(AttendanceRecord message);
    void onUpdateFailure(String message);
}
