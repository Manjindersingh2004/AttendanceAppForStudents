package com.example.attendanceAppForStudents;

import java.util.ArrayList;

public class GroupDataModel {

    String GroupName;
    String Percentage;
    ArrayList<AttendanceDataModel> Attendance;

    public GroupDataModel(String groupName, String percentage, ArrayList<AttendanceDataModel> attendance) {
        GroupName = groupName;
        Percentage = percentage;
        Attendance = attendance;
    }
}
