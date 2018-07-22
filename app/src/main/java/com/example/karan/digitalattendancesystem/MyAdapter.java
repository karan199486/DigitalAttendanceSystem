package com.example.karan.digitalattendancesystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder>
{
    List<StudentDetails> list;
    Context context;

    MyAdapter(List<StudentDetails> list, Context context)
    {
        this.list =list;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.student_list_row_layout,parent,false);

        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder,int position)
    {
        final int p= position;
        holder.rollno.setText(list.get(position).getRollno());
        holder.subject.setText("Subject : "+list.get(position).getSubject());
        holder.attendancecount.setText("Attendance Count : "+list.get(position).getAttendancecount());
        holder.course.setText(list.get(position).getCourse());
        holder.imgview_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context,MapsActivity.class);
                i.putExtra("longitude",list.get(p).getLongitude());
                i.putExtra("latitude",list.get(p).getLatitude());
                i.putExtra("rollno",list.get(p).getRollno());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView rollno,course,subject,attendancecount;
        ImageView imgview_location;


        public CustomViewHolder(View itemView) {
            super(itemView);
            rollno = itemView.findViewById(R.id.row_txt_rollno);
            course = itemView.findViewById(R.id.row_txt_course);
            subject = itemView.findViewById(R.id.row_txt_subject);
            attendancecount = itemView.findViewById(R.id.row_txt_attendancecount);
            imgview_location = itemView.findViewById(R.id.imgview_location);
        }

    }


}
