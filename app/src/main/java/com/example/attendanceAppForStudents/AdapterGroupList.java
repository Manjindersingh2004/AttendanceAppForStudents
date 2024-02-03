package com.example.attendanceAppForStudents;


import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class AdapterGroupList extends RecyclerView.Adapter<AdapterGroupList.ViewHolder> {


    Context context;
    ArrayList<GroupDataModel> group_list = new ArrayList<>();
    String collageId;
    String rollno;


    AdapterGroupList(Context context, ArrayList<GroupDataModel> group_list,String collageId,String rollno) {
        this.context = context;
        this.group_list = group_list;
        this.rollno=rollno;
        this.collageId=collageId;

    }

    @NonNull
    @Override
    public AdapterGroupList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.group_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroupList.ViewHolder holder, int position) {

        holder.group.setText(group_list.get(position).GroupName);
        int lecture=group_list.get(position).Attendance.size();
        int progress=Integer.parseInt(group_list.get(position).Percentage);
        holder.per.setText(progress+"%");
//        holder.progressBar.setProgress(progress);
        holder.lectures.setText("Lecture: "+lecture);

//        if(progress<75){
//            holder.progressBar.setIndicatorColor(ContextCompat.getColor(context,R.color.red_));
//        }
//        else{
//            holder.progressBar.setIndicatorColor(ContextCompat.getColor(context,R.color.green_));
//        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(NetworkUtils.isNetworkAvailable(context)){
                String group=group_list.get(holder.getAdapterPosition()).GroupName;
                context.startActivity(new Intent(context,ViewAttendanceActivity.class).putExtra("key",group).putExtra("collageId",collageId).putExtra("rollno",rollno).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            else{
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView group,per,students,lectures;
        //ProgressBar progressBar;

        CircularProgressIndicator progressBar;
        CardView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            group=itemView.findViewById(R.id.group_progress_name);
            per=itemView.findViewById(R.id.percentage_group_progress);
//            progressBar=itemView.findViewById(R.id.progress_bar);
            lectures=itemView.findViewById(R.id.group_progress_total_lecture);
            item=itemView.findViewById(R.id.group_progress_card);
        }
    }




}

