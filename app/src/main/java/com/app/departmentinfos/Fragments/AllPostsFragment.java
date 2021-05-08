package com.app.departmentinfos.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.ForumActivity;
import com.app.departmentinfos.Adapter.ForumAdapter;
import com.app.departmentinfos.Adapter.MyCoursesAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
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

import static android.content.Context.MODE_PRIVATE;

public class AllPostsFragment extends Fragment{
    Dialog dialog;
    TextView textViewUserName, textViewDate, textViewDescription,
            textViewName, textViewCommentDate;
    ImageView imageViewBlog,imageViewUser;
    EditText editTextComment;
    Button buttonCancel, buttonAddPost;
    File file=null;
    RecyclerView recyclerViewForum;
    FloatingActionButton floatingActionButton;
    ForumAdapter forumAdapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    ConnectionDetector connectionDetector;
    MultipartBody.Part fileToUpload;
    String department = "", code = "", name = "", type = "",image="",section="",designation="";
    String TAG = getClass().getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assignments,container,false);
        recyclerViewForum = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.floatingBtn);
        connectionDetector = new ConnectionDetector(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        code = sharedPreferences.getString(Config.CODE, "");
        name = sharedPreferences.getString(Config.USER_NAME, "");
        type = sharedPreferences.getString(Config.TYPE, "");
        section = sharedPreferences.getString(Config.SECTION, "");
        designation = sharedPreferences.getString(Config.DESIGNATION, "");
        image = sharedPreferences.getString(Config.IMAGE, "");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addForumDialog();
            }
        });
        if (connectionDetector.isConnectingToInternet())
            getForums();
        else
            new AlertDialog.Builder(getContext())
                    .setTitle("Internet Connection Err!!")
                    .setMessage("Internet not available, Check your internet connectivity and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    }).create().show();
        return view;
    }

    private void addForumDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_comment);
        textViewName = dialog.findViewById(R.id.nameTv);
        textViewCommentDate = dialog.findViewById(R.id.dateTv);
        editTextComment = dialog.findViewById(R.id.commentEt);
        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        buttonAddPost = dialog.findViewById(R.id.commentBtn);
        imageViewBlog = dialog.findViewById(R.id.forumIv);
        imageViewUser = dialog.findViewById(R.id.userIv);
        Picasso.get().load("https://myfinalproject24.com/android/img/"+image).placeholder(R.drawable.student).into(imageViewUser);
        imageViewBlog.setVisibility(View.VISIBLE);
        buttonAddPost.setText("Add");
        editTextComment.setHint("Write Something...");
        textViewName.setText(name);
        if (!image.isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+image).into(imageViewUser);
        }

        textViewCommentDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        imageViewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasty.warning(getContext(),"Haa").show();
//                PickImageDialog.build(new PickSetup()).show(((ForumActivity)getContext()).getSupportFragmentManager());
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult pickResult) {
                                //TODO: do what you have to...
                                if (pickResult.getError() == null) {
                                    file = new File(pickResult.getPath());
                                    imageViewBlog.setImageBitmap(pickResult.getBitmap());
                                } else {
                                    Toast.makeText(getContext(), pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(((ForumActivity)getContext()).getSupportFragmentManager());
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextComment.getText().toString().isEmpty()) {
                    editTextComment.setError("Please Write Something");
                } else {
                    if (editTextComment.getText().toString().isEmpty()) {
                        editTextComment.setError("Blog Can't be Empty");
                    } else if (connectionDetector.isConnectingToInternet()){
                        addPost();
                    }

                    else
                        new AlertDialog.Builder(getContext())
                                .setTitle("Internet Connection Err!!")
                                .setMessage("Internet not available, Check your internet connectivity and try again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                }).create().show();
                }
            }
        });

        dialog.show();

    }

    public void addPost() {
        if (file==null){
            fileToUpload = null;
        }else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }

        ServerCalling.postStudentBlog(
                fileToUpload,
                RequestBody.create(MediaType.parse("text/plain"), image),
                RequestBody.create(MediaType.parse("text/plain"), name),
                RequestBody.create(MediaType.parse("text/plain"), UUID.randomUUID().toString().substring(27)),
                RequestBody.create(MediaType.parse("text/plain"), editTextComment.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), code),
                RequestBody.create(MediaType.parse("text/plain"), textViewCommentDate.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), type.equalsIgnoreCase("teacher")?designation:section),
                RequestBody.create(MediaType.parse("text/plain"), department),
                new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.isSuccessful() && response.body().getValue().equals("success")) {
                            dialog.dismiss();
                            getForums();
                            Toasty.success(getContext(), "Successfully posted.Wait for admin approval").show();
                        } else {
                            Toasty.warning(getContext(), "Blog Can't Be Posted").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        dialog.dismiss();
                        Toasty.warning(getContext(), "Data Not Found").show();
                    }
                });
    }

    private void getForums() {
        ServerCalling.getForums(department,"", new Callback<List<Forum>>() {
            @Override
            public void onResponse(Call<List<Forum>> call, Response<List<Forum>> response) {
                if (response.body() != null || response.isSuccessful()) {
                    Collections.reverse(response.body());
                    forumAdapter = new ForumAdapter((ArrayList<Forum>) response.body(), getContext());
                    recyclerViewForum.setAdapter(forumAdapter);
                    recyclerViewForum.setLayoutManager(layoutManager);
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
