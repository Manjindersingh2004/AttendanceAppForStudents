package com.example.attendanceAppForStudents;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView nameTxt,rollnoTxt;
    RecyclerView recyclerView;
//    String rollno="12101012";
//    String collageId="12345";
    LinearLayout nothingHere;
    ProgressBar progress;
    String USERS="USERS";
    String STUDENTS="STUDENTS";
    AppCompatButton settings;

    StudentData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTxt=findViewById(R.id.nameTxt);
        rollnoTxt=findViewById(R.id.rollnoTxt);
        recyclerView=findViewById(R.id.recycler_view_group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nothingHere=findViewById(R.id.nothing_is_here_linear_layout);
        progress=findViewById(R.id.progressMain);
        settings=findViewById(R.id.settingsBtn);
        //fetchData();


        settings.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class).putExtra("name",nameTxt.getText().toString()));
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        nothingHere.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child(USERS).child(STUDENTS).child(FirebaseAuth.getInstance().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    data=snapshot.getValue(StudentData.class);
                    if(data!=null){
                        setAdapterAndChangeUi();
                    }else{
                        nothingHere.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                }else{
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    Toast.makeText(MainActivity.this, "Account Deleted due to incorrect information", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    void setAdapterAndChangeUi(){
        new MyFireBaseHelper(getApplicationContext()).retrieveData(data.CollageId, data.Rollno, nothingHere,progress,new MyFireBaseHelper.OnGetData() {
            @Override
            public void onGetData(ArrayList<GroupDataModel> groupDataModelArrayList,String name) {
                new MyFireBaseHelper(getApplicationContext()).displayData(groupDataModelArrayList);
                rollnoTxt.setText(data.Rollno);
                nameTxt.setText(name);
                AdapterGroupList adapter=new AdapterGroupList(getApplicationContext(),groupDataModelArrayList, data.CollageId, data.Rollno);
                recyclerView.setAdapter(adapter);
                //Toast.makeText(MainActivity.this, "on start", Toast.LENGTH_SHORT).show();
            }
        });
    }
}