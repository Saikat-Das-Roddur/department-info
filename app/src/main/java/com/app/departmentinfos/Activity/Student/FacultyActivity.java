package com.app.departmentinfos.Activity.Student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.departmentinfos.Adapter.FacultyAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Faculty;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyActivity extends AppCompatActivity {
    FacultyAdapter teacherListAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    String TAG = getClass().getSimpleName();
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    String department="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        recyclerView = findViewById(R.id.teacherRecyclerView);
        connectionDetector = new ConnectionDetector(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences= getSharedPreferences(Config.SHARED_PREF_NAME,MODE_PRIVATE);
        department = sharedPreferences.getString(Config.DEPARTMENT,"");
        //Toasty.warning(this,department).show();
        if (connectionDetector.isConnectingToInternet()){
            getFaculty();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }

    }

    private void getFaculty() {
        ServerCalling.getFaculty(department, new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    teacherListAdapter = new FacultyAdapter((ArrayList<Faculty>) response.body(), FacultyActivity.this);
                    recyclerView.setAdapter(teacherListAdapter);
                    //Toasty.warning(FacultyActivity.this,"Data").show();
                }
                else
                    Toasty.warning(FacultyActivity.this,"Data Not Found").show();
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {
                Toasty.warning(FacultyActivity.this,"Data Not Found").show();
            }
        });
    }

    public void goBack(View view) {

            startActivity(new Intent(FacultyActivity.this, StudentActivity.class));
            finish();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FacultyActivity.this, StudentActivity.class));
        finish();
    }
}