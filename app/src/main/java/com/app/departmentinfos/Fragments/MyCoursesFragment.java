package com.app.departmentinfos.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Adapter.AllCoursesAdapter;
import com.app.departmentinfos.Adapter.MyCoursesAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    String type = "",code="",section="";
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    RecyclerView.LayoutManager layoutManager;
    MyCoursesAdapter myCoursesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        connectionDetector = new ConnectionDetector(getContext());
        floatingActionButton.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        //Toasty.warning(context,"Ttdhkb").show();
        if (connectionDetector.isConnectingToInternet())
        getRegisteredCourses();
        else
            new AlertDialog.Builder(getContext())
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) getContext()).finish();
                        }
                    }).create().show();
        return view;
    }

    private void getRegisteredCourses() {
        ServerCalling.getRegisteredCourses(code, new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                if (response.body()!=null){
                    Collections.reverse(response.body());
                    myCoursesAdapter = new MyCoursesAdapter((ArrayList<Courses>) response.body(),getContext());
                    recyclerView.setAdapter(myCoursesAdapter);
                    myCoursesAdapter.notifyDataSetChanged();
                }
                //else
               // Toasty.warning(getContext(),"Course Not Added By Admin").show();
            }

            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Toasty.warning(getContext(),"Data Not Found").show();
            }
        });
    }
}
