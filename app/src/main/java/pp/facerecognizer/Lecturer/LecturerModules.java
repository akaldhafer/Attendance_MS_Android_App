package pp.facerecognizer.Lecturer;

public class LecturerModules {
    private String TeacherName ,TeacherID, TeacherPassword, TeacherEmail, Token;

    public LecturerModules() {

    }

    public LecturerModules(String teacherName, String teacherID, String teacherPassword, String teacherEmail, String token) {
        TeacherName = teacherName;
        TeacherID = teacherID;
        TeacherPassword = teacherPassword;
        TeacherEmail = teacherEmail;
        Token = token;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String teacherID) {
        TeacherID = teacherID;
    }

    public String getTeacherPassword() {
        return TeacherPassword;
    }

    public void setTeacherPassword(String teacherPassword) {
        TeacherPassword = teacherPassword;
    }

    public String getTeacherEmail() {
        return TeacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        TeacherEmail = teacherEmail;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
