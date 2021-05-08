package com.app.departmentinfos.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Adapter.AdminNoticeAdapter;
import com.app.departmentinfos.Adapter.CourseNoticeAdapter;
import com.app.departmentinfos.Adapter.SectionNoticeAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseNoticesFragment extends Fragment {
    Dialog dialog;
    TextView textViewTitle, textViewDate, textViewDescription,
            textViewName, textViewPostDate,textViewSection;
    ImageView imageViewUser;
    EditText editTextPost, editTextSearch;
    Button buttonCancel, buttonPost;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    CourseNoticeAdapter courseNoticeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    String type = "", section = "", department = "",name="",code="",image="";
    ArrayList<String> sections=new ArrayList<>();
    ArrayList<Notice> notices=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        editTextSearch = view.findViewById(R.id.searchEt);
        editTextSearch.setVisibility(View.VISIBLE);
        connectionDetector = new ConnectionDetector(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        section = sharedPreferences.getString(Config.SECTION, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        type = sharedPreferences.getString(Config.TYPE, "");
        name = sharedPreferences.getString(Config.USER_NAME, "");
        code = sharedPreferences.getString(Config.CODE, "");
        image = sharedPreferences.getString(Config.IMAGE, "");
        editTextSearch.setFocusable(false);
        editTextSearch.setClickable(true);
        if (type.equalsIgnoreCase("teacher")){
            editTextSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerCalling.getCourses(code, "", new Callback<List<Courses>>() {
                        @Override
                        public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                            sections.clear();
                            Log.d("TAG", "onResponse: " + response.body());
                            if (response.body() != null) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    sections.add(response.body().get(i).getTitle());
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Course List");
                            builder.setIcon(R.drawable.school);

                            builder.setItems(sections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editTextSearch.setText(response.body().get(i).getTitle());
                                    filter(response.body().get(i).getTitle());

                                }
                            }).show();
                        }

                        @Override
                        public void onFailure(Call<List<Courses>> call, Throwable t) {

                        }
                    });
                }
            });
        }else{
            editTextSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerCalling.getRegisteredCourses(code, new Callback<List<Courses>>() {
                        @Override
                        public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                            sections.clear();
                            Log.d("TAG", "onResponse: " + response.body());
                            if (response.body() != null) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    sections.add(response.body().get(i).getTitle());
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Course List");
                            builder.setIcon(R.drawable.school);

                            builder.setItems(sections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editTextSearch.setText(response.body().get(i).getTitle());
                                    filter(response.body().get(i).getTitle());

                                }
                            }).show();
                        }

                        @Override
                        public void onFailure(Call<List<Courses>> call, Throwable t) {

                        }
                    });
                }
            });
        }
        editTextSearch.setHint("Search Assignments");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });
        if (connectionDetector.isConnectingToInternet()){
            if (type.equalsIgnoreCase("student")){
                floatingActionButton.setVisibility(View.GONE);
                getCourseNoticeStudent();
            }else {
                getCourseNoticeTeacher();
            }

        }else
            new AlertDialog.Builder(getContext())
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity)getContext()).finish();
                        }
                    }).create().show();

        return view;
    }

    private void showNoticeDialog() {
        dialog =new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_comment);

        textViewName = dialog.findViewById(R.id.nameTv);
        textViewPostDate = dialog.findViewById(R.id.dateTv);
        editTextPost = dialog.findViewById(R.id.commentEt);
        textViewSection = dialog.findViewById(R.id.sectionTv);
        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        buttonPost = dialog.findViewById(R.id.commentBtn);
        imageViewUser = dialog.findViewById(R.id.userIv);
        if (!image.isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+image).into(imageViewUser);
        }

        textViewName.setText(name);
        textViewPostDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        textViewSection.setHint("Enter course");
        textViewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toasty.warning(getContext(),department).show();
                ServerCalling.getCourses(code,"",new Callback<List<Courses>>() {
                    @Override
                    public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                        sections.clear();
                        Log.d("TAG", "onResponse: " + response.body());
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                sections.add(response.body().get(i).getTitle());
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Course List");
                        builder.setIcon(R.drawable.school);

                        builder.setItems(sections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                textViewSection.setText(sections.get(i));

                            }
                        }).show();
                        //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
                    }

                    @Override
                    public void onFailure(Call<List<Courses>> call, Throwable t) {
                        Toasty.warning(getContext(), "Data Not Found").show();
                    }
                });

            }
        });

        editTextPost.setHint("Enter your post...");
        buttonPost.setText("Post");

        if (type.equalsIgnoreCase("teacher")){
            textViewSection.setVisibility(View.VISIBLE);
        }
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()){
                    if (type.equalsIgnoreCase("teacher")) {
                        if (textViewSection.getText().toString().isEmpty()) {
                            textViewSection.setError("Please enter course");
                        } else if (editTextPost.getText().toString().isEmpty()) {
                            editTextPost.setError("Please write something");
                        } else {
                            section = textViewSection.getText().toString();
                            postNotice();
                        }
                    } else {
                        if (editTextPost.getText().toString().isEmpty()) {
                            editTextPost.setError("Please write something");
                        } else {
                            postNotice();
                        }
                    }
                }else
                    new AlertDialog.Builder(getContext())
                            .setTitle("Internet Connection Err!!")
                            .setMessage("Internet not available, Check your internet connectivity and try again")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) getContext()).finish();
                                }
                            }).create().show();

            }
        });
        dialog.show();
    }

    private void postNotice() {
        ServerCalling.postNotices(
                UUID.randomUUID().toString().substring(27),
                editTextPost.getText().toString(),
                name, code,
                textViewPostDate.getText().toString(), section, department,image, new Callback<Notice>() {
                    @Override
                    public void onResponse(Call<Notice> call, Response<Notice> response) {
                        if (response.body().getValue().equalsIgnoreCase("success")){
                            dialog.dismiss();
                            getCourseNoticeTeacher();
                            Toasty.success(getContext(),"Posted successfully").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Notice> call, Throwable t) {
                        dialog.dismiss();
                        getCourseNoticeTeacher();
                        Toasty.success(getContext(),"Posted successfully").show();
                    }
                });
    }

//    private void getSectionNoticesTeacher() {
//        ServerCalling.getNoticesNonAdmin(code, new Callback<List<Notice>>() {
//            @Override
//            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
//                if (response.body()!=null){
//                    Collections.reverse(response.body());
//                    sectionNoticeAdapter = new SectionNoticeAdapter((ArrayList<Notice>) response.body(),getContext());
//                    recyclerView.setAdapter(sectionNoticeAdapter);
//                    sectionNoticeAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Notice>> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void getSectionNoticesStudent() {
//        ServerCalling.getNoticesSection(section,department,new Callback<List<Notice>>() {
//            @Override
//            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
//                if (response.body()!=null){
//                    Collections.reverse(response.body());
//                    sectionNoticeAdapter = new SectionNoticeAdapter((ArrayList<Notice>) response.body(),getContext());
//                    recyclerView.setAdapter(sectionNoticeAdapter);
//                    sectionNoticeAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Notice>> call, Throwable t) {
//
//            }
//        });
//    }
     private void getCourseNoticeTeacher() {
        ServerCalling.getCourseNoticeTeacher(code,department,new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Collections.reverse(response.body());
                    //notices = (ArrayList<Notice>) response.body();
                    courseNoticeAdapter = new CourseNoticeAdapter((ArrayList<Notice>) response.body(), getContext());
                    recyclerView.setAdapter(courseNoticeAdapter);
                    courseNoticeAdapter.notifyDataSetChanged();
                    notices = (ArrayList<Notice>) response.body();

                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {

            }
        });
    }

    private void getCourseNoticeStudent() {
        ServerCalling.getCourseNotice(code,department,new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Collections.reverse(response.body());
                    courseNoticeAdapter = new CourseNoticeAdapter((ArrayList<Notice>) response.body(), getContext());
                    recyclerView.setAdapter(courseNoticeAdapter);
                    courseNoticeAdapter.notifyDataSetChanged();
                    notices = (ArrayList<Notice>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {

            }
        });
    }

    public void filter(String text){
        if (notices!=null){
            ArrayList<Notice> temp = new ArrayList<>();
            for (Notice notice: notices){
                if (notice.getSection().toLowerCase().contains(text.toLowerCase())||
                       notice.getPostingDate().toLowerCase().contains(text.toLowerCase()))
                    temp.add(notice);
            }
            courseNoticeAdapter.updateList(temp);
        }

    }
}
