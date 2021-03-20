package pp.facerecognizer.Students;

public class StudentModules {
    private String studentName ,studentID, Password, studentCourse, Token, imageUri;
    public StudentModules() {
    }

    public StudentModules(String studentName, String studentID, String Password, String studentCourse, String token, String imageUri) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.Password = Password;
        this.studentCourse = studentCourse;
        this.imageUri = imageUri;
        Token = token;
    }
    public StudentModules(String studentID,String imageUri) {
        this.studentID = studentID;
        this.imageUri = imageUri;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public StudentModules(String toString) {
        this.imageUri = toString;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }


    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(String studentCourse) {
        this.studentCourse = studentCourse;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
