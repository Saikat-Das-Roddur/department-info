package com.app.departmentinfos.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.CoursesTabsAdapter;
import com.app.departmentinfos.Adapter.ForumAdapter;
import com.app.departmentinfos.Adapter.ForumTabsAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Fragments.AllPostsFragment;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    Dialog dialog;
    TextView textViewUserName, textViewDate, textViewDescription,
            textViewName, textViewCommentDate;
    ImageView imageViewBlog,imageViewUser;
    EditText editTextComment;
    Button buttonCancel, buttonAddPost;
    File file=null;
    ConnectionDetector connectionDetector;
    TabLayout tabLayout;
    ViewPager viewPager;
    String type = "", code="",name="",image="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE,"");
        code = sharedPreferences.getString(Config.CODE,"");
        image = sharedPreferences.getString(Config.IMAGE,"");
        connectionDetector = new ConnectionDetector(this);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("My Posts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ForumTabsAdapter tabsAdapter = new ForumTabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
    }


    public void goBack(View view) {
        if (type.equalsIgnoreCase("student")) {
            startActivity(new Intent(ForumActivity.this, StudentActivity.class));
            finish();
        } else {
            startActivity(new Intent(ForumActivity.this, TeacherActivity.class));
            finish();
        }
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
}