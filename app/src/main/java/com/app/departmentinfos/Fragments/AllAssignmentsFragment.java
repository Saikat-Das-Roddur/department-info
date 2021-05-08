package com.app.departmentinfos.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.AssignmentActivity;
import com.app.departmentinfos.Activity.SignInActivity;
import com.app.departmentinfos.Adapter.AllAssignmentAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Models.Department;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAssignmentsFragment extends Fragment {
    Dialog dialog;
    EditText  editTextDescription,editTextSearch;
    TextView textViewTitle, textViewCourseCode, textViewDate, textViewSubmissionDate,textViewSection;
    Button buttonCancel, buttonAdd;
    Calendar calendar;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ConnectionDetector connectionDetector;
    String type = "", code = "", section = "",department;
    SharedPreferences sharedPreferences;
    ArrayList<String> sections = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    AllAssignmentAdapter allAssignmentAdapter;
    ArrayList<Assignment> assignments = new ArrayList<>();
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments, container, false);
        context = getContext();
        editTextSearch = view.findViewById(R.id.searchEt);
        editTextSearch.setVisibility(View.VISIBLE);
        connectionDetector = new ConnectionDetector(context);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE, "");
        code = sharedPreferences.getString(Config.CODE, "");
        section = sharedPreferences.getString(Config.SECTION, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        calendar = Calendar.getInstance();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

//        editTextSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filter(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        if (type.equalsIgnoreCase("student")) {
            floatingActionButton.setVisibility(View.GONE);
            if (connectionDetector.isConnectingToInternet())
            getAssignmentBySection();
            else
                new AlertDialog.Builder(context)
                        .setTitle("Internet Connection Err!!")
                        .setMessage("Internet not available, Check your internet connectivity and try again")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity)context).finish();
                            }
                        }).create().show();
        } else {
            getAssignmentByCode();
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (connectionDetector.isConnectingToInternet())
                    showTeacherAssignmentDialog();
                    else
                        new AlertDialog.Builder(context)
                                .setTitle("Internet Connection Err!!")
                                .setMessage("Internet not available, Check your internet connectivity and try again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity)context).finish();
                                    }
                                }).create().show();
                }
            });
        }
        return view;
    }


    public void getAssignmentByCode() {
        ServerCalling.getAssignment(code, new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                assignments.clear();
                if (response.body() != null && response.isSuccessful()) {
                    Collections.reverse(response.body());
                    allAssignmentAdapter = new AllAssignmentAdapter((ArrayList<Assignment>) response.body(), getContext());
                    recyclerView.setAdapter(allAssignmentAdapter);
                    allAssignmentAdapter.notifyDataSetChanged();
                    assignments = (ArrayList<Assignment>)response.body();
                } else {
                    Toasty.warning(getContext(), "No Assignment").show();
                }

            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {

            }
        });
    }

    public void getAssignmentBySection() {
        //Toasty.warning(getContext(), section).show();
        ServerCalling.getAssignmentStudent(code, new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                assignments.clear();
                if (response.body() != null && response.isSuccessful()) {
                    Collections.reverse(response.body());
                    allAssignmentAdapter = new AllAssignmentAdapter((ArrayList<Assignment>) response.body(), getContext());
                    recyclerView.setAdapter(allAssignmentAdapter);
                    allAssignmentAdapter.notifyDataSetChanged();
                    assignments = (ArrayList<Assignment>) response.body();
                } else {
                    Toasty.warning(getContext(), "No Assignment").show();
                }
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {
                Toasty.warning(getContext(), "Data Not Found").show();
            }
        });
    }

    private void showTeacherAssignmentDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_assignment_teacher);

        textViewTitle = dialog.findViewById(R.id.titleTv);
        editTextDescription = dialog.findViewById(R.id.descEt);
        textViewSubmissionDate = dialog.findViewById(R.id.submissionDateTv);
        textViewSection = dialog.findViewById(R.id.sectionTv);
        textViewSection.setVisibility(View.GONE);
        textViewCourseCode = dialog.findViewById(R.id.courseCodeTv);
        textViewDate = dialog.findViewById(R.id.dateTv);
        textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));


        textViewSubmissionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        textViewSubmissionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

                    }

                }, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        textViewTitle.setOnClickListener(new View.OnClickListener() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Course List");
                        builder.setIcon(R.drawable.school);

                        builder.setItems(sections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                textViewTitle.setText(response.body().get(i).getTitle());
                                textViewCourseCode.setText(response.body().get(i).getCourseCode());
                            }
                        }).show();
                    }

                    @Override
                    public void onFailure(Call<List<Courses>> call, Throwable t) {

                    }
                });
            }
        });

//        textViewSection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toasty.warning(getContext(),department).show();
//                ServerCalling.getSection("",department,new Callback<List<Department>>() {
//                    @Override
//                    public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
//                        sections.clear();
//                        Log.d("TAG", "onResponse: " + response.body());
//                        if (response.body() != null) {
//                            for (int i = 0; i < response.body().size(); i++) {
//                                sections.add(response.body().get(i).getSectionName());
//                            }
//                        }
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("Section List");
//                        builder.setIcon(R.drawable.school);
//
//                        builder.setItems(sections.toArray(new String[0]), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                textViewSection.setText(sections.get(i));
//
//                            }
//                        }).show();
//                        //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Department>> call, Throwable t) {
//                        Toasty.warning(context, "Data Not Found").show();
//                    }
//                });
//
//            }
//        });

        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        buttonAdd = dialog.findViewById(R.id.submitBtn);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        dialog.show();
    }

    private void checkValidation() {
        if (textViewTitle.getText().toString().isEmpty()) {
            textViewTitle.setError("Empty Course Code");
        } else if (editTextDescription.getText().toString().isEmpty()) {
            editTextDescription.setError("Empty Assignment Description");
        } else if (textViewCourseCode.getText().toString().isEmpty()) {
            textViewCourseCode.setError("Empty Assignment Title");
        }
//        else if (textViewSection.getText().toString().isEmpty()) {
//            textViewSection.setError("Empty Section");
//        }
        else if (textViewSubmissionDate.getText().toString().isEmpty()) {
            textViewSubmissionDate.setError("Empty Submission Date");
        } else {
            if (connectionDetector.isConnectingToInternet())
             postAssignment();
            else
                new AlertDialog.Builder(context)
                        .setTitle("Internet Connection Err!!")
                        .setMessage("Internet not available, Check your internet connectivity and try again")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity)context).finish();
                            }
                        }).create().show();
        }
    }

    private void postAssignment() {
        ServerCalling.submitAssignmentsTeacher(UUID.randomUUID().toString().substring(27),
                textViewCourseCode.getText().toString(), code, textViewTitle.getText().toString(),
                editTextDescription.getText().toString(), "", "New",
                textViewDate.getText().toString(), textViewSubmissionDate.getText().toString(), new Callback<Teacher>() {
                    @Override
                    public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                        dialog.dismiss();
                        if (response.body().getValue().equalsIgnoreCase("success")) {
                            Toasty.success(getContext(), "Successfully Add Assignment").show();
                            getAssignmentByCode();
                        } else {
                            Toasty.warning(getContext(), "Assignment Not Added").show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Teacher> call, Throwable t) {
                        dialog.dismiss();
                        Toasty.warning(getContext(), "Data Not Found").show();
                    }
                });
    }

    public void filter(String text){
        if (assignments!=null){
            ArrayList<Assignment> temp = new ArrayList<>();
            for (Assignment assignment: assignments){
                if (assignment.getTitle().toLowerCase().contains(text.toLowerCase())||
                        assignment.getCourseCode().toLowerCase().contains(text.toLowerCase())
                        ||assignment.getPostingDate().toLowerCase().contains(text.toLowerCase()))
                    temp.add(assignment);
            }
            allAssignmentAdapter.updateList(temp);
        }

    }


}
