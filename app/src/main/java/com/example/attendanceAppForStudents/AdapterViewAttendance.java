package com.example.attendanceAppForStudents;


import android.content.Context;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class  AdapterViewAttendance extends RecyclerView.Adapter<AdapterViewAttendance.ViewHolder> {

    ArrayList<AttendanceDataModel> arraylist=new ArrayList<>();
    Context context;
    String key;



    AdapterViewAttendance(Context context,String key, ArrayList<AttendanceDataModel> arraylist){
        this.context=context;
        this.key=key;
        this.arraylist=arraylist;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_attendance_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color=R.color.c15;
        holder.attandanceCard.setCardBackgroundColor(ContextCompat.getColor(context, color));

        holder.date.setText(converDateToText(arraylist.get(position).Date));
        holder.status.setText(arraylist.get(position).Value);
        if(arraylist.get(position).Value.equals("Present")){
            holder.status.setTextColor(context.getResources().getColor(R.color.green_));
        }
        else{
            holder.status.setTextColor(context.getResources().getColor(R.color.red_));
        }


    }

    @Override
    public int getItemCount() {

        return arraylist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,status;
        CardView attandanceCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           date=itemView.findViewById(R.id.dateTxtItem);
           status=itemView.findViewById(R.id.statusTxtItem);
           attandanceCard=itemView.findViewById(R.id.recycler_view_attendance_layout);
        }
    }


    String converDateToText(String date) {
        String formattedDate=date;
        String[] parts = date.split("-");
        if (parts.length == 3) {
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];
            int month_num = Integer.parseInt(month);
            switch (month_num) {
                case 1:
                    month = " Jan, ";
                    break;
                case 2:
                    month = " Feb, ";
                    break;
                case 3:
                    month = " Mar, ";
                    break;
                case 4:
                    month = " Apr, ";
                    break;
                case 5:
                    month = " May, ";
                    break;
                case 6:
                    month = " June, ";
                    break;
                case 7:
                    month = " Jul, ";
                    break;
                case 8:
                    month = " Aug, ";
                    break;
                case 9:
                    month = " Sep, ";
                    break;
                case 10:
                    month = " Oct, ";
                    break;
                case 11:
                    month = " Nov, ";
                    break;
                case 12:
                    month = " Dec, ";
                    break;
                default:
                    month = " Invalid, ";
            }
            // Reformat the date in "DD-MM-YYYY" format
            formattedDate = day + month + year;
        }
        return formattedDate;
    }

}
