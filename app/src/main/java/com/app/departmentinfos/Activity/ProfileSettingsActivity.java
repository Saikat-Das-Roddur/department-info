package com.app.departmentinfos.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSettingsActivity extends AppCompatActivity implements IPickResult {

    ImageView imageViewUser;
    EditText editTextName, editTextEmail, editTextPassword, editTextContact;
    TextView textViewType, textViewCode, textViewDepartment, textViewSession, textViewSection ,textViewDesignation;
    Button buttonCancel, buttonUpdate;
    RelativeLayout relativeLayoutSection, relativeLayoutSession, relativeLayoutDesignation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<Student> studentData;
    List<Teacher> teacherData;
    File file;
    ConnectionDetector connectionDetector;
    MultipartBody.Part fileToUpload;
    String code = "", type = "",image="";
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        imageViewUser = findViewById(R.id.userIv);
        textViewType = findViewById(R.id.userTypeTv);
        textViewCode = findViewById(R.id.codeTv);
        textViewDepartment = findViewById(R.id.departmentTv);
        textViewSession = findViewById(R.id.sessionTv);
        textViewSection = findViewById(R.id.sectionTv);
        textViewDesignation = findViewById(R.id.designationTv);
        editTextName = findViewById(R.id.nameEt);
        editTextEmail = findViewById(R.id.emailEt);
        editTextPassword = findViewById(R.id.passwordEt);
        editTextContact = findViewById(R.id.contactEt);
        buttonCancel = findViewById(R.id.cancelBtn);
        buttonUpdate = findViewById(R.id.updateBtn);
        relativeLayoutSection = findViewById(R.id.sectionLayout);
        relativeLayoutSession = findViewById(R.id.sessionLayout);
        relativeLayoutDesignation = findViewById(R.id.designationLayout);
        connectionDetector = new ConnectionDetector(this);

        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        code = sharedPreferences.getString(Config.CODE, "");
        type = sharedPreferences.getString(Config.TYPE, "");
        image = sharedPreferences.getString(Config.IMAGE, "");
        if (connectionDetector.isConnectingToInternet()) {
            if (type.equals("student")) {
                textViewType.setText("Student");
                relativeLayoutDesignation.setVisibility(View.GONE);
                imageViewUser.setImageResource(R.drawable.student);
                getStudentProfile();
            } else {
                relativeLayoutSession.setVisibility(View.GONE);
                relativeLayoutSection.setVisibility(View.GONE);
                getTeacherProfile();
            }
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
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("student")) {
                    if (studentData != null) {
                        checkValidation();
                    }
                } else {
                    if (teacherData != null) {
                        checkValidation();
                    }
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("student")) {
                    startActivity(new Intent(ProfileSettingsActivity.this, StudentActivity.class));
                } else {
                    startActivity(new Intent(ProfileSettingsActivity.this, TeacherActivity.class));
                }
                finish();

            }
        });
        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toasty.warning(getApplicationContext(),"Haa").show();
                PickImageDialog.build(new PickSetup()).show((ProfileSettingsActivity.this).getSupportFragmentManager());
            }
        });
    }

    private void checkValidation() {

        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError("Name Can't be empty");
        } else if (editTextEmail.getText().toString().isEmpty() ||
                !editTextEmail.getText().toString().contains("@")
                ||
                !editTextEmail.getText().toString().contains(".")) {
            editTextEmail.setError("Invalid Email");
        } else if (editTextContact.getText().toString().length() != 11 ||
                !editTextContact.getText().toString().startsWith("01")) {
            editTextContact.setError("Invalid Contact");
        }
        else if (image.isEmpty()) {
            Toasty.warning(this,"Image File can't be empty").show();
        }
        else if (editTextPassword.getText().toString().length() < 4) {
            editTextPassword.setError("Password Must be 4 character");
        } else {
             if (connectionDetector.isConnectingToInternet()){
            updateStudent();
            }else{
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

    }

//    private void updateTeacher() {
//        ServerCalling.updateTeacher(editTextName.getText().toString(),
//                editTextEmail.getText().toString(), editTextContact.getText().toString(),
//                editTextPassword.getText().toString(), new Callback<Teacher>() {
//                    @Override
//                    public void onResponse(Call<Teacher> call, Response<Teacher> response) {
//                        if (response.body().getValue().equals("success")) {
//
//                            Toasty.success(ProfileSettingsActivity.this, "Profile Updated SuccessFully").show();
//                            startActivity(new Intent(ProfileSettingsActivity.this, TeacherActivity.class));
//                            finish();
//
//                        }else {
//                            Toasty.warning(ProfileSettingsActivity.this,"Can't Update profile").show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Teacher> call, Throwable t) {
//                        Toasty.warning(ProfileSettingsActivity.this,"Data Not Found").show();
//
//                    }
//                });
//    }

    private void updateStudent() {
        if (image.isEmpty()||file!=null){
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }
        ServerCalling.updateStudent(
                fileToUpload,
                RequestBody.create(MediaType.parse("text/plain"),code),
                RequestBody.create(MediaType.parse("text/plain"),editTextName.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"),editTextEmail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"),editTextContact.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"),editTextPassword.getText().toString()),
                new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.body().getValue().equals("success")) {

                            Toasty.success(ProfileSettingsActivity.this, "Profile Updated SuccessFully").show();
                            startActivity(new Intent(ProfileSettingsActivity.this, TeacherActivity.class));
                            finish();

                        } else {
                            Toasty.warning(ProfileSettingsActivity.this, "Can't Update profile").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        Toasty.warning(ProfileSettingsActivity.this, "Data Not Found").show();
                    }
                });
    }

    private void getTeacherProfile() {

        ServerCalling.getTeacherProfile(code, new Callback<List<Teacher>>() {
            @Override
            public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        Toasty.warning(ProfileSettingsActivity.this, "Problem Loading Data").show();
                    } else {
                        teacherData = response.body();
                        editTextName.setText(response.body().get(0).getName());
                        editTextEmail.setText(response.body().get(0).getEmail());
                        //editTextPassword.setText(response.body().get(0).getPassword());
                        editTextContact.setText(response.body().get(0).getContact());
                        textViewCode.setText(response.body().get(0).getCode());
                        textViewDesignation.setText(response.body().get(0).getDesignation());
                        //Toasty.warning(ProfileSettingsActivity.this,response.body().get(0).getFile()).show();
                        //imageViewUser.setImageURI(Uri.parse(Config.BASE_URL+"img/"+response.body().get(0).getFile()));
                        if (response.body().get(0).getFile().isEmpty()){
                            Picasso.get().load(R.drawable.teacher_2).into(imageViewUser);
                            //editor.putString(Config.IMAGE, "");
                        }

                        else{
                            Picasso.get().load(Config.BASE_URL+"img/"+response.body().get(0).getFile()).into(imageViewUser);
                            editor.putString(Config.IMAGE, response.body().get(0).getFile());
                            editor.commit();
                        }
                         //imageViewUser.set(response.body().get(0).getSes);
                        textViewDepartment.setText(response.body().get(0).getDept());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Teacher>> call, Throwable t) {
                Toasty.warning(ProfileSettingsActivity.this, "Data Not Found").show();
            }
        });
    }

    private void getStudentProfile() {
        ServerCalling.getStudentProfile(code, new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        Toasty.warning(ProfileSettingsActivity.this, "Problem Loading Data").show();
                    } else {
                        studentData = response.body();
                        editTextName.setText(response.body().get(0).getName());
                        editTextEmail.setText(response.body().get(0).getEmail());
                        //imageViewUser.setImageURI(Uri.parse(Config.BASE_URL+"img/"+response.body().get(0).getFile()));
                        //editTextPassword.setText(response.body().get(0).getPassword());
                        //Toasty.warning(ProfileSettingsActivity.this,Config.BASE_URL+"img/"+response.body().get(0).getFile()).show();
                        if (response.body().get(0).getFile().isEmpty()){
                            Picasso.get().load(R.drawable.student).into(imageViewUser);
                           // editor.putString(Config.IMAGE,"");
                        }

                        else{
                            Picasso.get().load(Config.BASE_URL+"img/"+response.body().get(0).getFile()).into(imageViewUser);
                            editor.putString(Config.IMAGE, response.body().get(0).getFile());
                            editor.commit();
                        }

                        editTextContact.setText(response.body().get(0).getContact());
                        textViewCode.setText(response.body().get(0).getCode());
                        textViewSection.setText(response.body().get(0).getSection());
                        textViewSession.setText(response.body().get(0).getSession());
                        textViewDepartment.setText(response.body().get(0).getDept());
                        Log.d(TAG, "onResponseProfile:"+response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toasty.warning(ProfileSettingsActivity.this, "Data Not Found").show();
            }
        });
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
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            file = new File(pickResult.getPath());

            //Toasty.warning(ProfileSettingsActivity.this,fileFromImagePicker.toString()).show();
            imageViewUser.setImageBitmap(pickResult.getBitmap());

        } else {
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}