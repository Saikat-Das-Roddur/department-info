package com.app.departmentinfos.Activity.Teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.departmentinfos.Activity.CourseActivity;
import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Adapter.RequestedCourseAdapter;
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
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editTextSearch;
    RequestedCourseAdapter requestedCourseAdapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    String code = "",department="";
    ArrayList<Courses> courses = new ArrayList<>();
    ArrayList<String> course = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        recyclerView = findViewById(R.id.recyclerView);
        editTextSearch = findViewById(R.id.searchEt);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        department = sharedPreferences.getString(Config.DEPARTMENT,"");
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        editTextSearch.setFocusable(false);
        editTextSearch.setClickable(true);
        editTextSearch.setHint("Search Course");
        editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalling.getRegisteredCourses(code, new Callback<List<Courses>>() {
                    @Override
                    public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                        course.clear();
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                course.add(response.body().get(i).getTitle());
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
                        builder.setTitle("Course List");
                        builder.setIcon(R.drawable.school);

                        builder.setItems(course.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                editTextSearch.setText(response.body().get(i).getTitle());


                            }
                        }).show();

                    }

                    @Override
                    public void onFailure(Call<List<Courses>> call, Throwable t) {

                    }
                });
            }
        });
        if (connectionDetector.isConnectingToInternet())
        getRequestedCourses();
            //Log.d("okay", "onCreate: ");
        else
            new AlertDialog.Builder(this)
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
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

    private void getRequestedCourses() {
        ServerCalling.getRegisteredCourses(code, new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    Log.d("TAG", "onResponseHaha: "+response.body() );

                    Collections.reverse(response.body());
                    requestedCourseAdapter = new RequestedCourseAdapter((ArrayList<Courses>) response.body(),RequestActivity.this);
                    recyclerView.setAdapter(requestedCourseAdapter);
                    requestedCourseAdapter.notifyDataSetChanged();
                    courses = (ArrayList<Courses>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Toasty.warning(RequestActivity.this,"Now in request").show();
            }
        });
    }

    public void goBack(View view) {

            startActivity(new Intent(RequestActivity.this, TeacherActivity.class));
            finish();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RequestActivity.this, TeacherActivity.class));
        finish();
    }
    public void filter(String text){
        if (courses!=null){
            ArrayList<Courses> temp = new ArrayList<>();
            for (Courses course: courses){
                if (course.getTitle().toLowerCase().contains(text.toLowerCase())||
                        course.getCourseCode().toLowerCase().contains(text.toLowerCase())
                        ||course.getStudentCode().toLowerCase().contains(text.toLowerCase()))
                    temp.add(course);
            }
            requestedCourseAdapter.updateList(temp);
        }

    }
}