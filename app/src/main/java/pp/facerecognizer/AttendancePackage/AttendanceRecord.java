package pp.facerecognizer.AttendancePackage;

import java.util.ArrayList;

import pp.facerecognizer.AssignCourse;

public class AttendanceRecord {
    private  String Intake, LecturerID, CourseID, Token, TimeDuration, NumberOfClasses;
    private  ArrayList<String> StudentIDs;
    private  ArrayList<String> StudentAttended;

    private  int ClassNo;
    private String Available;

    public ArrayList<String> getStudentAttended() {
        return StudentAttended;
    }

    public void setStudentAttended(ArrayList<String> studentAttended) {
        StudentAttended = studentAttended;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getNumberOfClasses() {
        return NumberOfClasses;
    }

    public void setNumberOfClasses(String numberOfClasses) {
        NumberOfClasses = numberOfClasses;
    }

    public int getClassNo() {
        return ClassNo;
    }

    public void setClassNo(int classNo) {
        ClassNo = classNo;
    }


    public AttendanceRecord(String intake, String lecturerID, String courseID,
                            String token, String timeDuration, String numberOfClasses,
                            ArrayList<String> studentIDs, ArrayList<String> studentAttended,
                            int classNo, String available) {
        Intake = intake;
        LecturerID = lecturerID;
        CourseID = courseID;
        Token = token;
        TimeDuration = timeDuration;
        NumberOfClasses = numberOfClasses;
        StudentIDs = studentIDs;
        StudentAttended = studentAttended;
        ClassNo = classNo;
        Available = available;
    }

    public ArrayList<String> getStudentIDs() {
        return StudentIDs;
    }

    public void setStudentIDs(ArrayList<String> studentIDs) {
        StudentIDs = studentIDs;
    }

    public AttendanceRecord() {
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

    public AttendanceRecord(AssignCourse assignCourse) {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getTimeDuration() {
        return TimeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        TimeDuration = timeDuration;
    }
}
