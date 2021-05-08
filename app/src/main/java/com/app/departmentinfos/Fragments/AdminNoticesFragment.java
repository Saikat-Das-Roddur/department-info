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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Adapter.AdminNoticeAdapter;
import com.app.departmentinfos.Adapter.SectionNoticeAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNoticesFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    AdminNoticeAdapter adminNoticeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    String code = "", section = "", department = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        connectionDetector = new ConnectionDetector(getContext());
        floatingActionButton.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        if (connectionDetector.isConnectingToInternet())
        getAdminNotice();
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
        return view;
    }

    private void getAdminNotice() {
        ServerCalling.getNoticesAdmin(department,new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Collections.reverse(response.body());
                    adminNoticeAdapter = new AdminNoticeAdapter((ArrayList<Notice>) response.body(), getContext());
                    recyclerView.setAdapter(adminNoticeAdapter);
                    adminNoticeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {

            }
        });
    }
}
