package com.app.departmentinfos.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.AssignmentActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Fragments.AllAssignmentsFragment;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Others.FilePathListener;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAssignmentAdapter extends RecyclerView.Adapter<AllAssignmentAdapter.Holder> {
    List<Assignment> assignments = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String type="";
    public AllAssignmentAdapter(ArrayList<Assignment> assignments, Context context) {
        this.assignments = assignments;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE,"");
    }

    @NonNull
    @Override
    public AllAssignmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.all_assignments_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllAssignmentAdapter.Holder holder, int position) {
        if (type.equalsIgnoreCase("teacher")){
            holder.textViewStatus.setText("Delete");
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.textViewStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAssignment(assignments.get(position).getAssignmentCode(),position);

                }
            });
        }

        holder.textViewDate.setText(assignments.get(position).getPostingDate());
        holder.textViewCourseCode.setText(assignments.get(position).getCourseCode());
        holder.textViewTitle.setText(assignments.get(position).getTitle());
       // if (type.equalsIgnoreCase("student")){
            holder.cardViewAssignments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ( (AssignmentActivity)context).showStudentAssignmentDialog(assignments.get(position));
                }
            });
        //}
    }


    private void deleteAssignment(String assignmentCode,int position) {
        ServerCalling.deleteAssignment(assignmentCode, new Callback<Assignment>() {
            @Override
            public void onResponse(Call<Assignment> call, Response<Assignment> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                  //  dialog.dismiss();
                    //new AllAssignmentsFragment().getAssignmentByCode();
                    assignments.remove(position);
                    notifyDataSetChanged();
                    Toasty.success(context, "Assignment deleted successfully").show();
                }
                else{
                   // dialog.dismiss();
                    Toasty.warning(context, "Unable to delete").show();

                }
            }

            @Override
            public void onFailure(Call<Assignment> call, Throwable t) {
                Toasty.warning(context, "Data not found").show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public void updateList(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        notifyDataSetChanged();
    }


    public class Holder extends RecyclerView.ViewHolder {
        Uri filepath;
        TextView textViewTitle,textViewDate,textViewCourseCode,textViewStatus;
        CardView cardViewAssignments;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.titleTv);
            textViewDate=itemView.findViewById(R.id.dateTv);
            textViewCourseCode=itemView.findViewById(R.id.courseCodeTv);
            textViewStatus=itemView.findViewById(R.id.statusTv);
            cardViewAssignments=itemView.findViewById(R.id.assignmentsCard);
        }
    }
}
