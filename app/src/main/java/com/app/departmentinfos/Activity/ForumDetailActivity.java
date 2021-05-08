package com.app.departmentinfos.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Adapter.CommentAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Comments;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumDetailActivity extends AppCompatActivity {
    Dialog dialog;
    TextView textViewTitle, textViewDate, textViewDescription,
            textViewName, textViewCommentDate;
    ImageView imageViewBlog,imageViewUser,imageViewCommenter;
    EditText editTextComment;
    Button buttonCancel, buttonComment;
    WebView webView;
    FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerViewComment;
    CommentAdapter commentAdapter;
    Comments comment;
    ConnectionDetector connectionDetector;
    ArrayList<Comments> comments;
    RecyclerView.LayoutManager layoutManagerComment;
    Forum forum;
    String name = "", code = "", type = "",image="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        // webView = findViewById(R.id.webView);
        textViewTitle = findViewById(R.id.titleTv);
        textViewDescription = findViewById(R.id.descTv);
        textViewDate = findViewById(R.id.dateTv);
        imageViewBlog = findViewById(R.id.blogImage);
        imageViewUser = findViewById(R.id.userIv);
        floatingActionButton = findViewById(R.id.floatingBtn);
        recyclerViewComment = findViewById(R.id.commentRecyclerView);
        connectionDetector = new ConnectionDetector(this);
        layoutManagerComment = new LinearLayoutManager(this);
        recyclerViewComment.setLayoutManager(layoutManagerComment);
        forum = getIntent().getParcelableExtra("forum");
        comment = getIntent().getParcelableExtra("comment");
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString(Config.USER_NAME, "");
        code = sharedPreferences.getString(Config.CODE, "");
        type = sharedPreferences.getString(Config.TYPE, "");
        image = sharedPreferences.getString(Config.IMAGE, "");
        Log.d("TAG", "onCreate: "+forum.toString());
        if (forum != null) {
            textViewTitle.setText(forum.getUser_name());
            textViewDate.setText(forum.getDesignation().isEmpty()?forum.getDate():forum.getDesignation()+"\n"+forum.getDate());
            textViewDescription.setText(forum.getDescription());
            editor.putString(Config.BLOGGER_CODE,forum.getCode());
            //Toasty.warning(this,forum.getCode()).show();
            editor.commit();
            if (!forum.getUserImage().isEmpty()){
                Picasso.get().load("https://myfinalproject24.com/android/img/"+forum.getUserImage()).into(imageViewUser);
            }
            if (forum.getFile().isEmpty()){
                imageViewBlog.setVisibility(View.GONE);
            }else{
               if (forum.getUser_name().equalsIgnoreCase("admin")){
                   Picasso.get().load("https://myfinalproject24.com/admin/img/"+forum.getFile()).into(imageViewBlog);
               }else
                   Picasso.get().load("https://myfinalproject24.com/android/img/"+forum.getFile()).into(imageViewBlog);
            }
           } else {
            Toasty.warning(this, "Data Not found").show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
        if (connectionDetector.isConnectingToInternet())
            getComment();
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

    }

    public void showCommentDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_comment);
        imageViewCommenter = dialog.findViewById(R.id.userIv);
        textViewName = dialog.findViewById(R.id.nameTv);
        textViewCommentDate = dialog.findViewById(R.id.dateTv);
        editTextComment = dialog.findViewById(R.id.commentEt);
        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        buttonComment = dialog.findViewById(R.id.commentBtn);
        if (!image.isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+image).into(imageViewCommenter);
        }
        if (comment != null) {
            textViewName.setText(comment.getUserName());
            textViewCommentDate.setText(comment.getDate());
            editTextComment.setText(comment.getComment());
        } else {
            textViewName.setText(name);
            textViewCommentDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        }
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextComment.getText().toString().isEmpty()) {
                    editTextComment.setError("Please Write Something");
                } else {
                    if (connectionDetector.isConnectingToInternet())
                        postComment();
                    else
                        new AlertDialog.Builder(ForumDetailActivity.this)
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
        });

        dialog.show();

    }

    private void postComment() {
        ServerCalling.postStudentComment(forum.getForumCode(), code, name, textViewCommentDate.getText().toString(),
                editTextComment.getText().toString(),image, new Callback<Comments>() {
                    @Override
                    public void onResponse(Call<Comments> call, Response<Comments> response) {
                        if (response.body().getValue().equals("success")) {
                            dialog.dismiss();
                            getComment();
                            Toasty.success(ForumDetailActivity.this, "Comment Successfully").show();
                        } else {
                            dialog.dismiss();
                            Toasty.warning(ForumDetailActivity.this, "Can't Comment").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comments> call, Throwable t) {
                        dialog.dismiss();
                        Toasty.warning(ForumDetailActivity.this, "Data Not Found").show();
                    }
                });
    }

    private void getComment() {
        ServerCalling.getForumComments(forum.getForumCode(), new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentAdapter = new CommentAdapter((ArrayList<Comments>) response.body(), ForumDetailActivity.this);
                    recyclerViewComment.setAdapter(commentAdapter);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {

            }
        });
    }

    public void goBack(View view) {

        startActivity(new Intent(this, ForumActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, ForumActivity.class));

        finish();
    }
}