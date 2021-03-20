package pp.facerecognizer.AssignedCourses;

import java.util.ArrayList;

import pp.facerecognizer.AssignCourse;

public class AssignedCourse {
    String Intake, LecturerID, CourseID, Token;
    ArrayList<String> StudentIDs;
    String ClassNo;

    public AssignedCourse(String courseIntake, String lecturerID, String courseID,
                          String token, ArrayList<String>studentIDs, String classNo) {
        Intake = courseIntake;
        LecturerID = lecturerID;
        CourseID = courseID;
        Token = token;
        StudentIDs = studentIDs;
        ClassNo = classNo;
    }

    public ArrayList<String> getStudentIDs() {
        return StudentIDs;
    }

    public void setStudentIDs(ArrayList<String> studentIDs) {
        StudentIDs = studentIDs;
    }

    public AssignedCourse() {
    }

    public String getIntake() {
        return Intake;
    }

    public void setIntake(String intake) {
        Intake = intake;
    }

    public String getLecturerID() {
        return LecturerID;
    }

    public void setLecturerID(String lecturerID) {
        LecturerID = lecturerID;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    public AssignedCourse(AssignCourse assignCourse) {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }



    public String getClassNo() {
        return ClassNo;
    }

    public void setClassNo(String classNo) {
        ClassNo = classNo;
    }


}
