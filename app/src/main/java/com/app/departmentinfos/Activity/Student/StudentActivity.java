package com.app.departmentinfos.Activity.Student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.departmentinfos.Activity.AssignmentActivity;
import com.app.departmentinfos.Activity.CourseActivity;
import com.app.departmentinfos.Activity.ForumActivity;
import com.app.departmentinfos.Activity.MessageActivity;
import com.app.departmentinfos.Activity.NoticeActivity;
import com.app.departmentinfos.Activity.ProfileSettingsActivity;
import com.app.departmentinfos.Activity.SignInActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardViewCourses, cardViewNotices,
            cardViewAssignments,cardViewTeachers,
            cardViewForum,cardViewMessages, cardViewSettings, cardViewExit;
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        cardViewAssignments = findViewById(R.id.assignmentsCard);
        cardViewCourses = findViewById(R.id.courseCard);
        cardViewNotices = findViewById(R.id.noticeCard);
        cardViewExit = findViewById(R.id.logoutCard);
        cardViewForum = findViewById(R.id.forumCard);
        cardViewTeachers = findViewById(R.id.teacherCard);
        cardViewMessages = findViewById(R.id.messageCard);
        cardViewSettings = findViewById(R.id.settingsCard);
        connectionDetector= new ConnectionDetector(this);
        sharedPreferences =getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Creating editor to store values to shared preferences
        editor = sharedPreferences.edit();

        cardViewSettings.setOnClickListener(this);
        cardViewAssignments.setOnClickListener(this);
        cardViewCourses.setOnClickListener(this);
        cardViewTeachers.setOnClickListener(this);
        cardViewExit.setOnClickListener(this);
        cardViewForum.setOnClickListener(this);
        cardViewNotices.setOnClickListener(this);
        cardViewMessages.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (connectionDetector.isConnectingToInternet()){
            switch (v.getId()){
                case R.id.courseCard:
                    startActivity(new Intent(this, CourseActivity.class));
                    finish();
                    break;
                case R.id.noticeCard:
                    startActivity(new Intent(this, NoticeActivity.class));
                    finish();
                    break;
                case R.id.assignmentsCard:
                    startActivity(new Intent(this, AssignmentActivity.class));
                    finish();
                    break;
                case R.id.teacherCard:
                    startActivity(new Intent(this, FacultyActivity.class));
                    finish();
                    break;
                case R.id.forumCard:
                    startActivity(new Intent(this, ForumActivity.class));
                    finish();
                    break;
                case R.id.messageCard:
                    startActivity(new Intent(this, MessageActivity.class));
                    finish();
                    break;
                case R.id.settingsCard:
                    startActivity(new Intent(this, ProfileSettingsActivity.class));
                    finish();
                    break;
                case R.id.logoutCard:
                    sharedPreferences.edit().clear().commit();
//                    editor.putString(Config.CODE, "");
//                    editor.putString(Config.SECTION,"");
//                    editor.putString(Config.USER_NAME,"");
//                    editor.putString(Config.DEPARTMENT,"");
//                    editor.putString(Config.TYPE,"");
                    //editor.commit();
                    Intent intent = new Intent(StudentActivity.this, SignInActivity.class);
                    //Saving values to editor
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
            }
        }else{
            new AlertDialog.Builder(StudentActivity.this)
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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        StudentActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}