package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.R;

import java.util.ArrayList;

public class SubmittedAssignmentAdapter extends RecyclerView.Adapter<SubmittedAssignmentAdapter.Holder> {
    ArrayList<Assignment> assignments = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String type = "";

    public SubmittedAssignmentAdapter(ArrayList<Assignment> assignments, Context context) {
        this.assignments = assignments;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE, "");
    }

    @NonNull
    @Override
    public SubmittedAssignmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.my_assignments_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubmittedAssignmentAdapter.Holder holder, int position) {
        holder.textViewCourseCode.setText(assignments.get(position).getCourseCode());
        if (type.equals("teacher")) {
            holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewStatus.setText("Submitted By " + assignments.get(position).getCode());
        }
        holder.textViewTitle.setText(assignments.get(position).getTitle());
        holder.textViewDate.setText(assignments.get(position).getPostingDate());
        holder.cardViewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (type.equals("teacher")){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.BASE_URL + "pdf_files/" + assignments.get(position).getFile()));
                context.startActivity(browserIntent);
                //showAssignmentDialog();
                // }
            }
        });

    }

    private void showAssignmentDialog() {

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
        TextView textViewTitle, textViewDate, textViewCourseCode, textViewStatus;
        CardView cardViewAssignment;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewCourseCode = itemView.findViewById(R.id.courseCodeTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            cardViewAssignment = itemView.findViewById(R.id.assignmentsCard);
            textViewStatus.setVisibility(View.GONE);
        }
    }
}
