package com.example.attendanceAppForStudents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout nothingLayout;
    TextView Heading;
    String key;// group name
    ArrayList<GroupDataModel> arrayList=new ArrayList<>();
    AdapterViewAttendance adapter;

    AppCompatButton backButton;

    String rollno;
    String collageId;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        Intent i=getIntent();
        key=i.getStringExtra("key");// group name
        collageId=i.getStringExtra("collageId");
        rollno=i.getStringExtra("rollno");
        Heading=findViewById(R.id.Heading_view_attendance);
        backButton=findViewById(R.id.back_button_view_attendance);
        recyclerView=findViewById(R.id.recycler_view_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nothingLayout=findViewById(R.id.nothing_is_here_linear_layout_view_attendance);
        progress=findViewById(R.id.progressbarView);

        Heading.setText(key);
        new MyFireBaseHelper(getApplicationContext()).retrieveData(collageId, rollno,nothingLayout, progress,new MyFireBaseHelper.OnGetData() {
            @Override
            public void onGetData(ArrayList<GroupDataModel> groupDataModelArrayList, String name) {
                int i=0;
                ArrayList<AttendanceDataModel> arrayList=new ArrayList<>();
                for (i=0;i<groupDataModelArrayList.size();i++) {
                    if(groupDataModelArrayList.get(i).GroupName.equals(key)){
                        break;
                    }
                }
                ArrayList<AttendanceDataModel> attendanceList=groupDataModelArrayList.get(i).Attendance;
                if(attendanceList.isEmpty()){
                    nothingLayout.setVisibility(View.VISIBLE);
                }
                else{
                    nothingLayout.setVisibility(View.GONE);
                }
                adapter=new AdapterViewAttendance(getApplicationContext(),key,attendanceList);
                recyclerView.setAdapter(adapter);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}