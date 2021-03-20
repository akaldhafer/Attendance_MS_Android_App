package pp.facerecognizer.AssignedCourses;



public interface AssignedCourseViewFetch {

    void onUpdateUpuccess(AssignedCourse message);
    void onUpdateFailure(String message);
}
