package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.R;

import java.util.ArrayList;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.Holder> {
    ArrayList<Courses> courses = new ArrayList<>();
    Context context;

    public MyCoursesAdapter(ArrayList<Courses> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyCoursesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.all_assignments_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCoursesAdapter.Holder holder, int position) {
        holder.textViewCredit.setText("Semester: "+courses.get(position).getCourse_semester()+"\n"+"Section: "+courses.get(position).getSection());
        holder.textViewCourseCode.setText("Course Code: "+courses.get(position).getCourseCode());
        holder.textViewTitle.setText("Course Title: "+courses.get(position).getTitle());
        holder.textViewName.setText("Teacher Name: "+courses.get(position).getTeacherName());
        holder.textViewStatus.setText(courses.get(position).getStatus());
        if (courses.get(position).getStatus().equalsIgnoreCase("incomplete")){
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.red));
        }else if (courses.get(position).getStatus().equalsIgnoreCase("enrolled")){
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.yellow));
        }else if (courses.get(position).getStatus().equalsIgnoreCase("failed")){
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.red));
        }else {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewCourseCode, textViewCredit,textViewStatus,textViewName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewCourseCode = itemView.findViewById(R.id.courseCodeTv);
            textViewCredit = itemView.findViewById(R.id.dateTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            textViewName = itemView.findViewById(R.id.nameTv);
            textViewName.setVisibility(View.VISIBLE);
        }
    }
}
