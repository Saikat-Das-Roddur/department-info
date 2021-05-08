package com.app.departmentinfos.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.AdminNoticeAdapter;
import com.app.departmentinfos.Adapter.AssignmentTabsAdapter;
import com.app.departmentinfos.Adapter.NoticeTabsAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AdminNoticeAdapter adminNoticeAdapter;
    String TAG = getClass().getSimpleName();
    String type = "", code = "", department = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE, "");
        code = sharedPreferences.getString(Config.CODE, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");

        tabLayout.addTab(tabLayout.newTab().setText("Section"));
        tabLayout.addTab(tabLayout.newTab().setText("Course"));
        tabLayout.addTab(tabLayout.newTab().setText("Official"));

        // requestStoragePermission();
        //tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        NoticeTabsAdapter tabsAdapter = new NoticeTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);


    }

    private void getTeacherNotices() {
        ServerCalling.getNoticesAdmin(code, new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.body() != null) {
                    adminNoticeAdapter = new AdminNoticeAdapter((ArrayList<Notice>) response.body(), NoticeActivity.this);
                    recyclerView.setAdapter(adminNoticeAdapter);
                    adminNoticeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void goBack(View view) {
        if (type.equalsIgnoreCase("student")) {
            startActivity(new Intent(this, StudentActivity.class));
        } else {
            startActivity(new Intent(this, TeacherActivity.class));
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (type.equalsIgnoreCase("student")) {
            startActivity(new Intent(this, StudentActivity.class));
        } else {
            startActivity(new Intent(this, TeacherActivity.class));
        }
        finish();
    }

}