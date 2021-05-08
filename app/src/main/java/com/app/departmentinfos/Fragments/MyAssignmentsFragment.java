package com.app.departmentinfos.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.AssignmentActivity;
import com.app.departmentinfos.Adapter.SubmittedAssignmentAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAssignmentsFragment extends Fragment {
    SubmittedAssignmentAdapter assignmentAdapter;
    EditText editTextSearch;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    String code= "",department="",type = "";
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<Assignment> assignments = new ArrayList<>();
    ConnectionDetector connectionDetector;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_assignments,container,false) ;
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextSearch = view.findViewById(R.id.searchEt);
        editTextSearch.setVisibility(View.VISIBLE);
        connectionDetector = new ConnectionDetector(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        department = sharedPreferences.getString(Config.DEPARTMENT,"");
        type = sharedPreferences.getString(Config.TYPE,"");
        if (connectionDetector.isConnectingToInternet())
        getAssignments();
        else
            new AlertDialog.Builder(getContext())
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity)getContext()).finish();
                        }
                    }).create().show();
        editTextSearch.setFocusable(false);
        editTextSearch.setClickable(true);
        editTextSearch.setHint("Search Assignments");

        if (type.equalsIgnoreCase("teacher")){
            editTextSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerCalling.getCourses(code, "", new Callback<List<Courses>>() {
                        @Override
                        public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                            courses.clear();
                            Log.d("TAG", "onResponse: " + response.body());
                            if (response.body() != null) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    courses.add(response.body().get(i).getTitle());
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Course List");
                            builder.setIcon(R.drawable.school);

                            builder.setItems(courses.toArray(new String[0]), new DialogInterface.OnClickListener() {
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
                            courses.clear();
                            Log.d("TAG", "onResponse: " + response.body());
                            if (response.body() != null) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    courses.add(response.body().get(i).getTitle());
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Course List");
                            builder.setIcon(R.drawable.school);

                            builder.setItems(courses.toArray(new String[0]), new DialogInterface.OnClickListener() {
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
        return view;
    }


    private void deleteAssignment(String assignmentCode) {
        ServerCalling.deleteAssignment(assignmentCode, new Callback<Assignment>() {
            @Override
            public void onResponse(Call<Assignment> call, Response<Assignment> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                    //dialog.dismiss();
                    //new AllAssignmentsFragment().getAssignmentByCode();
                   // Toasty.success(AssignmentActivity.this, "Assignment deleted successfully").show();
                }
                else{
                   // dialog.dismiss();
                    //new AllAssignmentsFragment().getAssignmentByCode();
                    //Toasty.success(AssignmentActivity.this,response.body().getMessage() ).show();

                }
            }

            @Override
            public void onFailure(Call<Assignment> call, Throwable t) {

            }
        });
    }

    private void getAssignments() {
        ServerCalling.getSubmittedAssignments(code, new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    Collections.reverse(response.body());
                    assignmentAdapter = new SubmittedAssignmentAdapter((ArrayList<Assignment>) response.body(),getContext());
                    assignments = (ArrayList<Assignment>)response.body();
                    recyclerView.setAdapter(assignmentAdapter);
                    assignmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {

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
            assignmentAdapter.updateList(temp);
        }

    }

}
