package com.example.attendanceAppForStudents;

public class StudentData {
    String Rollno;
    String CollageId;

    StudentData(){

    }

    public StudentData(String rollno, String collageId) {
        Rollno = rollno;
        CollageId = collageId;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public String getCollageId() {
        return CollageId;
    }

    public void setCollageId(String collageId) {
        CollageId = collageId;
    }
}
