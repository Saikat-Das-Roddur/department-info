package com.app.departmentinfos.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RequestedCourseAdapter extends RecyclerView.Adapter<RequestedCourseAdapter.Holder> {
    Dialog dialog;
    TextView textViewTitle, textViewCourseCode, textViewDescription, textViewCredit, textViewAvailability,
            textViewTeacher,textViewSemester,textViewSection;
    Button buttonAccept, buttonReject;
    SharedPreferences sharedPreferences;
    String code = "",type;
    ArrayList<Courses> courses = new ArrayList<>();
    Context context;

    public RequestedCourseAdapter(ArrayList<Courses> courses, Context context) {
        this.courses = courses;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME,MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        type = sharedPreferences.getString(Config.TYPE,"");
    }

    @NonNull
    @Override
    public RequestedCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.request_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedCourseAdapter.Holder holder, int position) {
        holder.textViewTitle.setText(courses.get(position).getTitle());
        holder.textViewStudentID.setText("Student ID: "+courses.get(position).getStudentCode());
        holder.textViewCourseCode.setText(courses.get(position).getCourseCode());
        holder.textViewRequest.setText(courses.get(position).getRequest());
            if (courses.get(position).getRequest().equalsIgnoreCase("pending")){
                holder.textViewRequest.setTextColor(context.getResources().getColor(R.color.red));
            }else if (courses.get(position).getRequest().equalsIgnoreCase("accepted")){
                holder.textViewRequest.setTextColor(context.getResources().getColor(R.color.yellow));
            }else if (courses.get(position).getRequest().equalsIgnoreCase("failed")){
                holder.textViewRequest.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                holder.textViewRequest.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }





        holder.cardViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toasty.warning(context,courses.get(position).getStudentCode()).show();
                showCourseDialog(position);
            }
        });
    }

    private void showCourseDialog(int position) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_course);
        ImageView imageViewCourse;
        TextView textViewDescTitle,textViewCreditTitle, textViewTeacherTitle, textViewAvailabilityTitle;
        textViewAvailabilityTitle = dialog.findViewById(R.id.availableTitleTv);
        textViewDescTitle = dialog.findViewById(R.id.descTitleTv);
        textViewCreditTitle = dialog.findViewById(R.id.creditTitleTv);
        textViewTeacherTitle = dialog.findViewById(R.id.teacherTitleTv);
        imageViewCourse = dialog.findViewById(R.id.courseIv);
        textViewTitle = dialog.findViewById(R.id.titleTv);
        textViewCredit = dialog.findViewById(R.id.creditTv);
        textViewCourseCode = dialog.findViewById(R.id.courseCodeTv);
        textViewDescription = dialog.findViewById(R.id.descTv);
        textViewAvailability = dialog.findViewById(R.id.availableTv);
        textViewTeacher = dialog.findViewById(R.id.teacherTv);
        textViewSemester = dialog.findViewById(R.id.semesterTv);
        textViewSection = dialog.findViewById(R.id.sectionTv);
        buttonAccept = dialog.findViewById(R.id.addCourseBtn);
        buttonReject = dialog.findViewById(R.id.rejectBtn);
        textViewDescTitle.setText("Student ID");
        textViewCreditTitle.setText("Status");
        textViewTeacherTitle.setText("Request");
        textViewAvailabilityTitle.setVisibility(View.GONE);
        textViewAvailability.setVisibility(View.GONE);
        //buttonReject.setVisibility(View.VISIBLE);

        textViewTitle.setText(courses.get(position).getTitle());
        textViewCourseCode.setText(courses.get(position).getCourseCode());
        textViewCredit.setText(courses.get(position).getStatus());
        textViewDescription.setText(courses.get(position).getStudentCode());
        textViewAvailability.setText(courses.get(position).getAvailability());
        textViewSemester.setText(courses.get(position).getSemester());
        textViewSection.setText(courses.get(position).getSection());
        textViewTeacher.setText(courses.get(position).getRequest());
        if (courses.get(position).getRequest().equalsIgnoreCase("Pending")){
            buttonAccept.setText("Accept");
        }else if(courses.get(position).getRequest().equalsIgnoreCase("Accepted")){
            buttonReject.setVisibility(View.VISIBLE);
            buttonReject.setText("Fail");
            buttonAccept.setText("Complete");
        }else if(courses.get(position).getRequest().equalsIgnoreCase("Completed")){
            buttonAccept.setText("Completed");
            buttonAccept.setEnabled(false);
        }else {
            buttonAccept.setText("Failed");
            buttonAccept.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
            buttonAccept.setEnabled(false);
        }
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourseRequest(position, "Failed", "Failed");
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonAccept.getText().toString().equalsIgnoreCase("accept")){
                    updateCourseRequest(position, "Accepted", "Enrolled");
                }else if (buttonAccept.getText().toString().equalsIgnoreCase("complete")){
                    updateCourseRequest(position, "Completed", "Completed");
                }
            }
        });

        dialog.show();
    }

    private void updateCourseRequest(int position,String request, String status) {
        ServerCalling.updateRequestedCourses(courses.get(position).getStudentCode(),code, courses.get(position).getCourseCode(),status, request,
                new Callback<Courses>() {
                    @Override
                    public void onResponse(Call<Courses> call, Response<Courses> response) {
                        if (response.body().getValue().equalsIgnoreCase("success")){
                            dialog.dismiss();
                            Toasty.success(context,"Course Updated Successfully").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Courses> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateList(ArrayList<Courses> courseList) {
        this.courses = courseList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewCourseCode, textViewStudentID, textViewRequest;
        CardView cardViewCourse;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewCourseCode = itemView.findViewById(R.id.courseCodeTv);
            textViewStudentID = itemView.findViewById(R.id.studentCodeTv);
            textViewRequest = itemView.findViewById(R.id.requestTv);
            cardViewCourse = itemView.findViewById(R.id.courseCard);
            //cardViewCourse.setVisibility(View.GONE);
        }
    }
}
