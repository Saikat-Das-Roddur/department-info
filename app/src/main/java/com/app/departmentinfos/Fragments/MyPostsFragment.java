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

import com.app.departmentinfos.Activity.ForumActivity;
import com.app.departmentinfos.Adapter.ForumAdapter;
import com.app.departmentinfos.Adapter.MyCoursesAdapter;
import com.app.departmentinfos.Adapter.MyPostAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Forum;
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

public class MyPostsFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    String type = "",code="",section="";
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    RecyclerView.LayoutManager layoutManager;
    MyPostAdapter forumAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_assignments,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        connectionDetector = new ConnectionDetector(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        if (connectionDetector.isConnectingToInternet())
            getPosts();
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

    private void getPosts() {
        ServerCalling.getForums("", code, new Callback<List<Forum>>() {
            @Override
            public void onResponse(Call<List<Forum>> call, Response<List<Forum>> response) {
                if (response.body() != null || response.isSuccessful()) {
                    Collections.reverse(response.body());
                    forumAdapter = new MyPostAdapter((ArrayList<Forum>) response.body(), getContext());
                    recyclerView.setAdapter(forumAdapter);
                    recyclerView.setLayoutManager(layoutManager);
                    forumAdapter.notifyDataSetChanged();
                } else {
                    Toasty.warning(getContext(), "Data Not Found").show();
                }
            }

            @Override
            public void onFailure(Call<List<Forum>> call, Throwable t) {
                Toasty.warning(getContext(), "Data Not Found").show();
            }
        });
    }
}
