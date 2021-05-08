package com.app.departmentinfos.Fragments;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.MessageActivity;
import com.app.departmentinfos.Adapter.AllAssignmentAdapter;
import com.app.departmentinfos.Adapter.AllCoursesAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoursesFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    EditText editTextSearch;
    String type = "", code="",department="";
    SharedPreferences sharedPreferences;
    ConnectionDetector connectionDetector;
    RecyclerView.LayoutManager layoutManager;
    AllCoursesAdapter allCoursesAdapter;
    ArrayList<Courses> courses = new ArrayList<>();
    //ArrayList<String> courseList = new ArrayList<>();
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        editTextSearch = view.findViewById(R.id.searchEt);
        editTextSearch.setVisibility(View.VISIBLE);
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE,"");
        code = sharedPreferences.getString(Config.CODE,"");
        department = sharedPreferences.getString(Config.DEPARTMENT,"");
        connectionDetector  = new ConnectionDetector(context);
        floatingActionButton.setVisibility(View.GONE);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //Toasty.warning(context,"Ttdhkb").show();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // filter((String) s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (connectionDetector.isConnectingToInternet()){
            getCourses();

        }

        else
            new AlertDialog.Builder(context)
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) context).finish();
                        }
                    }).create().show();
        return view;

    }

    private void getCourses() {
        ServerCalling.getCourses("",department,new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                //Toasty.warning(context,"Ttdhkb").show();
                if (response.body()!=null){
                    Log.d("TAG", "getCourses: "+response.body().toString());
                    Collections.reverse(response.body());
                    courses = (ArrayList<Courses>)response.body();
                    allCoursesAdapter = new AllCoursesAdapter((ArrayList<Courses>) response.body(),getContext());

                    //Toasty.warning(context,"Ttdhkb").show();
                    recyclerView.setAdapter(allCoursesAdapter);
                    allCoursesAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Toasty.warning(getContext(),"Data Not Found").show();
            }
        });
    }
   public void filter(String text){
        if (courses!=null){
            ArrayList<Courses> temp = new ArrayList<>();
            for (Courses courses: courses){
                if (courses.getTitle().toLowerCase().contains(text.toLowerCase())||
                        courses.getCourseCode().toLowerCase().contains(text.toLowerCase())
                ||courses.getCredit().toLowerCase().contains(text.toLowerCase()))
                    temp.add(courses);
            }
            allCoursesAdapter.updateList(temp);
        }

   }
}
