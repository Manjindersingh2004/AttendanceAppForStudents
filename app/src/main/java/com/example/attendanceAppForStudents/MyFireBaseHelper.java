package com.example.attendanceAppForStudents;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFireBaseHelper {
    Context context;

    String COLLAGES="COLLAGES";
   // String COLLAGE_ID = "12345";


    String GROUPS = "GROUPS";
    String STUDENTS = "STUDENTS";
    String ATTENDANCE = "ATTENDANCE";
    String DATE="DATE";
    String NAME="NAME";
    String ROLLNO="ROLL_NO";
    String PERCENTAGE="PERCENTAGE";
    MyFireBaseHelper(Context context){
        this.context=context;
    }

    void retrieveData(String collageId, String rollNo, LinearLayout layout, ProgressBar progressBar, OnGetData onGetData) {
        DatabaseReference collagesRef = FirebaseDatabase.getInstance().getReference().child(COLLAGES).child(collageId).child(ROLLNO).child(rollNo);

        collagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data for the specified collageId and rollNo is available
                    String name = dataSnapshot.child(NAME).getValue().toString();

                    ArrayList<GroupDataModel> groupDataModelArrayList=new ArrayList<>();

                    for (DataSnapshot groupSnapshot : dataSnapshot.child(GROUPS).getChildren()) {
                        String groupName = groupSnapshot.getKey();
                        String percentage = groupSnapshot.child(PERCENTAGE).getValue().toString();

                        ArrayList<AttendanceDataModel> attendanceArrayList=new ArrayList<>();
                        for(DataSnapshot attendanceSnapShot: groupSnapshot.child(ATTENDANCE).getChildren()){
                            String Date= attendanceSnapShot.getKey();
                            String attendance=attendanceSnapShot.getValue().toString();
                            attendanceArrayList.add(new AttendanceDataModel(Date,attendance));
                        }
                        groupDataModelArrayList.add(new GroupDataModel(groupName,percentage,attendanceArrayList));
                    }

                    progressBar.setVisibility(View.GONE);

                   onGetData.onGetData(groupDataModelArrayList,name);


                } else {
                    // Data for the specified collageId and rollNo does not exist
                    Toast.makeText(context, "Invalid Rollno or Collage Id", Toast.LENGTH_SHORT).show();
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("RetrieveDataActivity", "Database Error: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    void displayData(ArrayList<GroupDataModel> groupDataModelArrayList) {
        String data ="";
        for (GroupDataModel groupDataModel : groupDataModelArrayList) {
            data+=(groupDataModel.GroupName)+": ";
            data+=(groupDataModel.Percentage)+"\n\n";

            // Print attendance data for each group
            for (AttendanceDataModel attendanceDataModel : groupDataModel.Attendance) {
                data+=(attendanceDataModel.Date)+": ";
                data+=(attendanceDataModel.Value)+"\n";
            }
        }
        //Toast.makeText(context, data, Toast.LENGTH_LONG).show();
    }


    interface OnGetData{
        default void onGetData(ArrayList<GroupDataModel> groupDataModelArrayList,String name){

        }
    }

    interface OnCheckExists{
        default void onCheckExists(boolean isExists){

        }
    }


    void isCollageRollExists(String collageId, String rollNo,ProgressBar progressBar, OnCheckExists onCheckExists) {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference collagesRef = FirebaseDatabase.getInstance().getReference().child(COLLAGES).child(collageId).child(ROLLNO).child(rollNo);

        collagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data for the specified collageId and rollNo is available

                    //progressBar.setVisibility(View.GONE);

                    onCheckExists.onCheckExists(true);


                } else {
                    onCheckExists.onCheckExists(false);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("RetrieveDataActivity", "Database Error: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
                onCheckExists.onCheckExists(false);
            }
        });
    }

}
