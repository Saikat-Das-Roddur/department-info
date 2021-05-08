package com.app.departmentinfos.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.CourseActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoursesAdapter extends RecyclerView.Adapter<AllCoursesAdapter.Holder> {
    Dialog dialog;
    TextView textViewTitle, textViewCourseCode, textViewDescription, textViewCredit, textViewAvailability,
            textViewTeacher,textViewSemester, textViewSection;
    Button buttonAddCourses;
    ArrayList<Courses> courses = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String code = "", type = "";

    public AllCoursesAdapter(ArrayList<Courses> courses, Context context) {
        this.courses = courses;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE, "");
        type = sharedPreferences.getString(Config.TYPE, "");
    }

    @NonNull
    @Override
    public AllCoursesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.course_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllCoursesAdapter.Holder holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + courses.get(position).toString());
        holder.textViewCreditHours.setText("Credit Hours: " + courses.get(position).getCredit());
        holder.textViewCourseCode.setText("Course Code: " + courses.get(position).getCourseCode());
        holder.textViewCourseTitle.setText(courses.get(position).getTitle());
        holder.cardViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        textViewTitle = dialog.findViewById(R.id.titleTv);
        textViewCredit = dialog.findViewById(R.id.creditTv);
        textViewSection = dialog.findViewById(R.id.sectionTv);
        textViewSemester = dialog.findViewById(R.id.semesterTv);
        textViewCourseCode = dialog.findViewById(R.id.courseCodeTv);
        textViewDescription = dialog.findViewById(R.id.descTv);
        textViewAvailability = dialog.findViewById(R.id.availableTv);
        textViewTeacher = dialog.findViewById(R.id.teacherTv);
        buttonAddCourses = dialog.findViewById(R.id.addCourseBtn);
        textViewTitle.setText(courses.get(position).getTitle());
        textViewCourseCode.setText(courses.get(position).getCourseCode());
        textViewCredit.setText(courses.get(position).getCredit());
        textViewDescription.setText(courses.get(position).getDesc());
        textViewAvailability.setText(courses.get(position).getAvailability());
        textViewTeacher.setText(courses.get(position).getTeacherName());
        textViewSection.setText(courses.get(position).getSection());
        textViewSemester.setText(courses.get(position).getCourse_semester());
//         if (courses.get(position).getAvailability().equalsIgnoreCase("available")){
//                    buttonAddCourses.setText("unavailable");
//                }else{
//                    buttonAddCourses.setText("available");
//                }
        if (textViewAvailability.getText().toString().equalsIgnoreCase("available")) {
            // buttonAddCourses.setVisibility(View.VISIBLE);
            if (type.equalsIgnoreCase("student")){
                buttonAddCourses.setText("Add Course");
                buttonAddCourses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            addCourses(position);

                        //updateCourses(position);
                    }
                });

            }else {
                buttonAddCourses.setText("Unavailable");
                //buttonAddCourses.setTextColor(Color.parseColor("#B71C1C"));
                buttonAddCourses.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                buttonAddCourses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCourse(position, buttonAddCourses.getText().toString());
                    }
                });

            }
            }
        else {
            if (type.equalsIgnoreCase("student")){
                buttonAddCourses.setVisibility(View.GONE);
            }else {
                buttonAddCourses.setText("Available");
                //buttonAddCourses.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                buttonAddCourses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCourse(position, buttonAddCourses.getText().toString());
                    }
                });

            }
        }
        dialog.show();
    }

    private void addCourses(int position) {
        ServerCalling.addCourses(code, courses.get(position).getTeacherCode(),courses.get(position).getTeacherName(), courses.get(position).getCourseCode(),courses.get(position).getTitle(),
                courses.get(position).getSemester(),courses.get(position).getCourse_semester(),courses.get(position).getSection(),
                "Incomplete", "Pending", new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                    dialog.dismiss();
                    Toasty.success(context,"Request Send successfully").show();
                }else {
                    dialog.dismiss();
                    Toasty.warning(context,"Request Already sent").show();
                }
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {
                dialog.dismiss();
                Toasty.warning(context,"Data Not Found").show();
            }
        });
    }

    private void updateCourse(int position,String availability) {
        ServerCalling.updateCourses(code, courses.get(position).getCourseCode(), availability,
                new Callback<Courses>() {
                    @Override
                    public void onResponse(Call<Courses> call, Response<Courses> response) {
                        if (response.body().getValue().equalsIgnoreCase("success")){
                            dialog.dismiss();
                            Toasty.success(context,"Course Updated Successfully").show();
                            ((CourseActivity)context).refreshLayout();

                        }
                    }

                    @Override
                    public void onFailure(Call<Courses> call, Throwable t) {

                    }
                });
    }
    public void updateList(ArrayList<Courses> list){
        courses = list;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return courses.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewCourseTitle, textViewCourseCode, textViewCreditHours;
        CardView cardViewCourse;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewCourseTitle = itemView.findViewById(R.id.titleTv);
            textViewCourseCode = itemView.findViewById(R.id.courseCodeTv);
            textViewCreditHours = itemView.findViewById(R.id.creditTv);
            cardViewCourse = itemView.findViewById(R.id.courseCard);
        }
    }

}
