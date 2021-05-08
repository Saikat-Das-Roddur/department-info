package com.app.departmentinfos.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.AllCoursesAdapter;
import com.app.departmentinfos.Adapter.AssignmentTabsAdapter;
import com.app.departmentinfos.Adapter.CoursesTabsAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText editTextSearch;
    String type = "", code="",name="";
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ArrayList<Courses> courses = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    AllCoursesAdapter allCoursesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        recyclerView = findViewById(R.id.recyclerView);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);
        editTextSearch = findViewById(R.id.searchEt);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE,"");
        code = sharedPreferences.getString(Config.CODE,"");
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        //depart = sharedPreferences.getString(Config.CODE,"");
        if (type.equalsIgnoreCase("teacher")){
            recyclerView.setVisibility(View.VISIBLE);
            editTextSearch.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            getCourses();
        }else{
           // recyclerView.setVisibility(View.VISIBLE);
            tabLayout.addTab(tabLayout.newTab().setText("All Courses"));
            tabLayout.addTab(tabLayout.newTab().setText("My Courses"));

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            CoursesTabsAdapter tabsAdapter = new CoursesTabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
            viewPager.setAdapter(tabsAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(this);
        }
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    public void refreshLayout(){
        getCourses();
    }

    private void getCourses() {
        ServerCalling.getCourses(code, "",new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                //Toasty.warning(context,"Ttdhkb").show();
                if (response.body()!=null){
                    Log.d("TAG", "getCourses: "+response.body().toString());
                    allCoursesAdapter = new AllCoursesAdapter((ArrayList<Courses>) response.body(),CourseActivity.this);
                    //Toasty.warning(context,"Ttdhkb").show();
                    courses = (ArrayList<Courses>)response.body();
                    recyclerView.setAdapter(allCoursesAdapter);
                    allCoursesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Toasty.warning(CourseActivity.this,"Data Not Found").show();
            }
        });
    }

    public void goBack(View view) {
        if (type.equalsIgnoreCase("student")){
            startActivity(new Intent(CourseActivity.this, StudentActivity.class));
            finish();
        }else{
            startActivity(new Intent(CourseActivity.this, TeacherActivity.class));
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        if (type.equalsIgnoreCase("student")){
            startActivity(new Intent(this, StudentActivity.class));

        }else{
            startActivity(new Intent(this, TeacherActivity.class));

        }
        finish();
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