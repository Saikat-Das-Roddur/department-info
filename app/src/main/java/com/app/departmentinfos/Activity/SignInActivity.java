package com.app.departmentinfos.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Department;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.github.*;

public class SignInActivity extends AppCompatActivity {

    TextView textViewLogInType, textViewOther, textViewSign,
            textViewDialogSignUp, textViewDepartment, textViewDialogOther, textViewTitle,
            textViewSection, textViewSession, textViewSemester;
    Button buttonTeacher, buttonStudent, buttonSignUp;
    ImageView imageViewUser;
    EditText editTextName, editTextEmail, editTextContact, editTextPassword, editTextCode;
    RelativeLayout relativeLayoutName, relativeLayoutEmail, relativeLayoutContact, relativeLayoutPassword,
            relativeLayoutMetricId, relativeLayoutSection, relativeLayoutSession, relativeLayoutSemester,relativeLayoutDepartment;
    LinearLayout linearLayoutSignIn, linearLayoutSignUp;
    Dialog dialog;

    String token = "", code = "", section = "", type="", department = "";
    String TAG = getClass().getSimpleName();
    ArrayList<String> departments = new ArrayList<>();
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        textViewLogInType = findViewById(R.id.logInTypeTv);
        buttonStudent = findViewById(R.id.studentBtn);
        buttonTeacher = findViewById(R.id.teacherBtn);
        linearLayoutSignUp = findViewById(R.id.signUpLayout);
        textViewOther = findViewById(R.id.otherTv);
        textViewSign = findViewById(R.id.signUpTv);
        connectionDetector = new ConnectionDetector(this);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        type = sharedPreferences.getString(Config.TYPE, "No");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast

                        //Toast.makeText(SignInActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


       // if (connectionDetector.isConnectingToInternet()) {
        //Toasty.warning(this,type).show();
            if (type.equalsIgnoreCase("student")) {
                Toasty.info(this, "You already signed in as student").show();
                Intent intent = new Intent(this, StudentActivity.class);
                startActivity(intent);
                finish();
            } else if (type.equalsIgnoreCase("teacher")) {
                Toasty.info(this, "You already signed in as teacher").show();
                Intent intent = new Intent(this, TeacherActivity.class);
                startActivity(intent);
                finish();
            }

        //}
        //else {
//            new AlertDialog.Builder(this)
//                    .setTitle("Internet Connection Err!!")
//                    .setMessage("Internet not available, Check your internet connectivity and try again")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }).create().show();
       // }
    }

    public void studentSignIn(View view) {
        editor.putString(Config.TYPE, "student");
        editor.commit();
        //((Button)view).getText().toString();
        // Toast.makeText(this, ((Button)view).getText().toString(), Toast.LENGTH_SHORT).show();

        signInDialog(((Button) view).getText().toString().toLowerCase());
    }

    public void teacherSignIn(View view) {
        // Toast.makeText(this, ((Button)view).getText().toString(), Toast.LENGTH_SHORT).show();
//        editTextCode.setHint("Enter your contact");
//        editTextCode.setInputType(InputType.TYPE_CLASS_PHONE);
        editor.putString(Config.TYPE, "teacher");
        editor.commit();
        signInDialog(((Button) view).getText().toString().toLowerCase());
    }

    public void signUp(View view) {
        if (buttonStudent.getText().toString().contains("in")) {
            buttonStudent.setText("sign up as student".toLowerCase());
            buttonTeacher.setText("sign up as teacher".toLowerCase());
            textViewLogInType.setText("Select Sign Up Type");
            textViewOther.setText("Have an account?");
            textViewSign.setText("Sign In");
        } else {
            buttonStudent.setText("sign in as student".toLowerCase());
            buttonTeacher.setText("sign in as teacher".toLowerCase());
            textViewLogInType.setText("Select Sign In Type");
            textViewOther.setText("Don't have an account?");
            textViewSign.setText("Sign Up");
        }
    }

    private void signInDialog(String text) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_sign_in);

        buttonSignUp = dialog.findViewById(R.id.signUpBtn);
        imageViewUser = dialog.findViewById(R.id.userIv);

        textViewTitle = dialog.findViewById(R.id.loginTv);
        textViewDialogSignUp = dialog.findViewById(R.id.signUpTv);
        textViewDialogOther = dialog.findViewById(R.id.otherTv);
        textViewDepartment = dialog.findViewById(R.id.departmentTv);

        editTextName = dialog.findViewById(R.id.nameEt);
        editTextContact = dialog.findViewById(R.id.contactEt);
        editTextCode = dialog.findViewById(R.id.metricIdEt);
        editTextEmail = dialog.findViewById(R.id.emailEt);
        editTextPassword = dialog.findViewById(R.id.passwordEt);
        textViewSection = dialog.findViewById(R.id.sectionTv);
        textViewSemester = dialog.findViewById(R.id.semesterTv);
        textViewSession = dialog.findViewById(R.id.sessionTv);

        relativeLayoutName = dialog.findViewById(R.id.nameLayout);
        relativeLayoutContact = dialog.findViewById(R.id.contactLayout);
        relativeLayoutDepartment = dialog.findViewById(R.id.departmentLayout);
        relativeLayoutEmail = dialog.findViewById(R.id.emailLayout);
        relativeLayoutMetricId = dialog.findViewById(R.id.metricIdLayout);
        relativeLayoutPassword = dialog.findViewById(R.id.passwordLayout);
        relativeLayoutSection = dialog.findViewById(R.id.sectionLayout);
        relativeLayoutSession = dialog.findViewById(R.id.sessionLayout);
        relativeLayoutSemester = dialog.findViewById(R.id.semesterLayout);

        linearLayoutSignIn = dialog.findViewById(R.id.signInLayout);

        linearLayoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.contains("up")) {
                    buttonStudent.setText("sign in as student".toLowerCase());
                    buttonTeacher.setText("sign in as teacher".toLowerCase());
                    textViewLogInType.setText("Select Sign In Type");
                    textViewOther.setText("Don't have an account?");
                    textViewSign.setText("Sign Up");
                } else {
                    buttonStudent.setText("sign up as student".toLowerCase());
                    buttonTeacher.setText("sign up as teacher".toLowerCase());
                    textViewLogInType.setText("Select Sign Up Type");
                    textViewOther.setText("Have an account?");
                    textViewSign.setText("Sign In");
                }
                dialog.dismiss();
            }
        });

        if (text.contains("up") && text.contains("teacher")) {
            relativeLayoutMetricId.setVisibility(View.GONE);
            relativeLayoutSession.setVisibility(View.GONE);
            relativeLayoutSection.setVisibility(View.GONE);
            relativeLayoutSemester.setVisibility(View.GONE);

        } else if (text.contains("in") && text.contains("teacher")) {
            editTextCode.setHint("Enter your contact");
            editTextCode.setInputType(InputType.TYPE_CLASS_PHONE);
            textViewDialogOther.setText("Don't have an account?");
            textViewDialogSignUp.setText("Sign Up");
            textViewTitle.setText("Sign In Teacher");
            buttonSignUp.setText("Sign In".toLowerCase());
            relativeLayoutSession.setVisibility(View.GONE);
            relativeLayoutSection.setVisibility(View.GONE);
            relativeLayoutSemester.setVisibility(View.GONE);
            relativeLayoutName.setVisibility(View.GONE);
            relativeLayoutContact.setVisibility(View.GONE);
            relativeLayoutEmail.setVisibility(View.GONE);
            relativeLayoutDepartment.setVisibility(View.GONE);

        } else if (text.contains("up") && text.contains("student")) {
            imageViewUser.setImageResource(R.drawable.student);
            textViewTitle.setText("Sign Up Student");
        } else if (text.contains("in") && text.contains("student")) {
            imageViewUser.setImageResource(R.drawable.student);
            textViewDialogOther.setText("Don't have an account?");
            textViewDialogSignUp.setText("Sign Up");
            textViewTitle.setText("Sign In Student");
            buttonSignUp.setText("Sign In".toLowerCase());
            relativeLayoutSession.setVisibility(View.GONE);
            relativeLayoutSection.setVisibility(View.GONE);
            relativeLayoutSemester.setVisibility(View.GONE);
            relativeLayoutName.setVisibility(View.GONE);
            relativeLayoutContact.setVisibility(View.GONE);
            relativeLayoutEmail.setVisibility(View.GONE);
            relativeLayoutDepartment.setVisibility(View.GONE);

        }
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    if (textViewTitle.getText().toString().contains("Up")) {
                        //dialog.dismiss();
                      //  if (textViewTitle.getText().toString().contains("Student")) {
                            checkValidation();
                       // } else if (textViewTitle.getText().toString().contains("Teacher")) {
                           // checkValidation();
                      //  }
                        buttonStudent.setText("sign in as student".toLowerCase());
                        buttonTeacher.setText("sign in as teacher".toLowerCase());
                        textViewLogInType.setText("Select Sign In Type");
                        textViewOther.setText("Don't have an account?");
                        textViewSign.setText("Sign Up");


                    } else if (textViewTitle.getText().toString().contains("In")) {
                        // dialog.dismiss();
                        //if (textViewTitle.getText().toString().contains("Student")) {
                            checkValidation();
                        //} else if (textViewTitle.getText().toString().contains("Teacher")) {
                            //checkValidation();
                        //}

                    }

                } else {
                    new AlertDialog.Builder(SignInActivity.this)
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
        textViewDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDepartments();
            }
        });
        textViewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSection();
            }
        });
        textViewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSession();
            }
        });
        textViewSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSemester();
            }
        });

        dialog.show();
       // doKeepDialog(dialog);
    }

    private void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void showSection() {
        if (textViewDepartment.getText().toString().isEmpty()){
            textViewDepartment.setError("Department can't be empty");
        } else if (textViewSemester.getText().toString().isEmpty()){
            textViewSemester.setError("Semester can't be empty");
        }
        else
        ServerCalling.getSection(textViewSemester.getText().toString(),textViewDepartment.getText().toString(),new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                departments.clear();
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        departments.add(response.body().get(i).getSectionName());
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle("Section List");
                builder.setIcon(R.drawable.school);

                builder.setItems(departments.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        textViewSection.setText(departments.get(i));

                    }
                }).show();
                //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toasty.warning(SignInActivity.this, "Data Not Found").show();
            }
        });


    }

    private void showSession() {
        if (textViewDepartment.getText().toString().isEmpty()){
            textViewDepartment.setError("Department can't be empty");
        }
        else
        ServerCalling.getSession(textViewDepartment.getText().toString(),new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                departments.clear();
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        departments.add(response.body().get(i).getSession());
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle("Session List");
                builder.setIcon(R.drawable.school);

                builder.setItems(departments.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        textViewSession.setText(departments.get(i));

                    }
                }).show();
                //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toasty.warning(SignInActivity.this, "Data Not Found").show();
            }
        });

    }

    private void showSemester() {
        String[] semester = new String[]{"1st","2nd","3rd","4th","5th","6th","7th","8th"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Semester List");
        builder.setIcon(R.drawable.school);

        builder.setItems(semester, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                textViewSemester.setText(semester[i]);

            }
        }).show();
//        ServerCalling.getSemester(new Callback<List<Department>>() {
//            @Override
//            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
//                departments.clear();
//                Log.d(TAG, "onResponse: " + response.body());
//                if (response.body() != null) {
//                    for (int i = 0; i < response.body().size(); i++) {
//                        departments.add(response.body().get(i).getSemester());
//                    }
//                }
//
//                //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
//            }
//
//            @Override
//            public void onFailure(Call<List<Department>> call, Throwable t) {
//                Toasty.warning(SignInActivity.this, "Data Not Found").show();
//            }
//        });

    }

    private void showDepartments() {
        //departments.clear();
        ServerCalling.getDepartments(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                departments.clear();
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        departments.add(response.body().get(i).getDepartmentName());
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle("Department List");
                builder.setIcon(R.drawable.school);

                builder.setItems(departments.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        textViewDepartment.setText(departments.get(i));

                    }
                }).show();
                //Toasty.success(SignInActivity.this,"Successfully Signed In").show();
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toasty.warning(SignInActivity.this, "Data Not Found").show();
            }
        });
    }

    private void checkValidation() {
        if (textViewTitle.getText().toString().contains("Up")) {
            if (textViewTitle.getText().toString().contains("Student")) {
                if (textViewDepartment.getText().toString().isEmpty()) {
                    textViewDepartment.setError("Department Can't be empty");
                } else if (editTextName.getText().toString().isEmpty()) {
                    editTextName.setError("Name Can't be empty");
                } else if (editTextEmail.getText().toString().isEmpty() ||
                        !editTextEmail.getText().toString().contains("@")
                        ||
                        !editTextEmail.getText().toString().contains(".")) {
                    editTextEmail.setError("Invalid Email");
                } else if (editTextContact.getText().toString().length() != 11 ||
                        !editTextContact.getText().toString().startsWith("01")) {
                    editTextContact.setError("Invalid Contact");
                } else if (editTextPassword.getText().toString().length() < 4) {
                    editTextPassword.setError("Password Must be 4 character");
                } else if (editTextCode.getText().toString().isEmpty()) {
                    editTextCode.setError("Id can't be empty");
                } else if (textViewSection.getText().toString().isEmpty()) {
                    textViewSection.setError("Section Can't be empty");
                } else if (textViewSession.getText().toString().isEmpty()) {
                    textViewSession.setError("Session Can't be empty");
                } else {
                    if (connectionDetector.isConnectingToInternet())
                        signUpStudent();
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
            } else if (textViewTitle.getText().toString().contains("Teacher")) {
                if (textViewDepartment.getText().toString().isEmpty()) {
                    textViewDepartment.setError("Department Can't be empty");
                } else if (editTextName.getText().toString().isEmpty()) {
                    editTextName.setError("Name Can't be empty");
                } else if (editTextEmail.getText().toString().isEmpty() ||
                        !editTextEmail.getText().toString().contains("@")
                        ||
                        !editTextEmail.getText().toString().contains(".")) {
                    editTextEmail.setError("Invalid Email");
                } else if (editTextContact.getText().toString().length() != 11 ||
                        !editTextContact.getText().toString().startsWith("01")) {
                    editTextContact.setError("Invalid Contact");
                } else if (editTextPassword.getText().toString().length() < 4) {
                    editTextPassword.setError("Password Must be 4 character");
                }  else {
                    signUpTeacher();
                }
            }
        }
        else if (textViewTitle.getText().toString().contains("In")) {
            // dialog.dismiss();
            if (textViewTitle.getText().toString().contains("Student")) {
            if (editTextPassword.getText().toString().length() < 4) {
                editTextPassword.setError("Password Must be 4 character");
            }
            else if (editTextCode.getText().toString().isEmpty()) {
                editTextCode.setError("Code can't be empty");
            } else {
                if (connectionDetector.isConnectingToInternet())
                    signInStudent();
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

             }
            else if (textViewTitle.getText().toString().contains("Teacher")) {

                if (editTextPassword.getText().toString().length() < 4) {
                    editTextPassword.setError("Password Must be 4 character");
                } else if (editTextCode.getText().toString().length()!=11 ||
                !editTextCode.getText().toString().startsWith("01")) {
                    editTextCode.setError("Id can't be empty");
                } else {
                    signInStudent();
                }
            }

        }

    }

//    private void signInTeacher() {
//        ServerCalling.teacherSignIn(editTextCode.getText().toString(),
//                editTextPassword.getText().toString(), token,
//                new Callback<Teacher>() {
//                    @Override
//                    public void onResponse(Call<Teacher> call, Response<Teacher> response) {
//                        dialog.dismiss();
//                        if (!response.body().getStatus().equalsIgnoreCase("Pending")){
//                            editor.putString(Config.CODE,editTextCode.getText().toString());
//                            editor.putString(Config.USER_NAME,response.body().getName());
//                            editor.putString(Config.DEPARTMENT,response.body().getDept());
//                            editor.putString(Config.TYPE,"teacher");
//                            editor.commit();
//                            Toasty.success(SignInActivity.this, "Successfully Signed In").show();
//                            startActivity(new Intent(SignInActivity.this,TeacherActivity.class));
//                            finish();
//                        } else{
//                            Log.d(TAG, "onResponse: "+call.request().body());
////                            progressDialog.dismiss();
//                            Toasty.warning(SignInActivity.this,"Make sure admin added you").show();
//                        }
//                   }
//
//                    @Override
//                    public void onFailure(Call<Teacher> call, Throwable t) {
//                        dialog.dismiss();
//                        Toasty.warning(SignInActivity.this, "Data Not Found").show();
//                    }
//                });
//    }

    private void signInStudent() {
        type = sharedPreferences.getString(Config.TYPE, "");
        ServerCalling.studentSignIn(editTextCode.getText().toString(),
                editTextPassword.getText().toString(), type, token,
                new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        dialog.dismiss();
                        Log.d(TAG, "onResponse: " + response.body().toString());
                        if (response.body().getValue().equalsIgnoreCase("success")) {
                            if (!response.body().getStatus().equalsIgnoreCase("Pending")) {
                                editor.putString(Config.CODE, editTextCode.getText().toString());
                                editor.putString(Config.USER_NAME, response.body().getName());
                                editor.putString(Config.DEPARTMENT, response.body().getDept());
                                editor.putString(Config.IMAGE, response.body().getFile());
                                Log.d(TAG, "onResponse: "+response.body());
                                if (response.body().getStatus().equalsIgnoreCase("student")) {
                                    Toasty.success(SignInActivity.this, "Successfully Signed In as student").show();
                                    //Toasty.success(SignInActivity.this, response.body().getFile()).show();
                                    editor.putString(Config.TYPE, "student");
                                    editor.putString(Config.SECTION, response.body().getSection());
                                    editor.commit();
                                    startActivity(new Intent(SignInActivity.this, StudentActivity.class));
                                    finish();
                                } else if (response.body().getStatus().equalsIgnoreCase("teacher")) {
                                    editor.putString(Config.DESIGNATION,response.body().getDesignation());
                                    editor.commit();
                                    Toasty.success(SignInActivity.this, "Successfully Signed In as teacher").show();
                                    startActivity(new Intent(SignInActivity.this, TeacherActivity.class));
                                    finish();
                                }


                            } else {
                                Log.d(TAG, "onResponse: " + call.request().body());
//                            progressDialog.dismiss();
                                Toasty.warning(SignInActivity.this, "Make sure admin added you").show();
                            }
                        } else {
                            dialog.dismiss();
                            Toasty.warning(SignInActivity.this, "Make sure you are signed up").show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        dialog.dismiss();
                        Toasty.warning(SignInActivity.this, "Data Not Found").show();
                    }
                });
    }

    private void signUpTeacher() {
        ServerCalling.teacherSignUp(editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextContact.getText().toString(),
                editTextPassword.getText().toString(),
                editTextContact.getText().toString(),
                textViewDepartment.getText().toString(),
                token, new Callback<Teacher>() {
                    @Override
                    public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                        if (response.body().getValue().equals("success")) {
                            dialog.dismiss();
                            Log.d(TAG, "onResponse: " + response.body());
                            Toasty.success(SignInActivity.this, "Sign up successful").show();
//                            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
//                            finish();
                        } else {
                            dialog.dismiss();
                            Toasty.warning(SignInActivity.this, "Already Signed Up!!").show();
                            //Toast.makeText(SignUpActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Teacher> call, Throwable t) {
                        dialog.dismiss();
                        Toasty.warning(SignInActivity.this, "Data Not Found").show();
                    }
                });
    }

    private void signUpStudent() {
        ServerCalling.studentSignUp(
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextContact.getText().toString(),
                editTextPassword.getText().toString(),
                editTextCode.getText().toString(),
                textViewSection.getText().toString(),
                textViewSession.getText().toString(),
                textViewDepartment.getText().toString(),
                token, new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.body().getValue().equals("success")) {
                            dialog.dismiss();
                            Log.d(TAG, "onResponse: " + response.body());
                            Toasty.success(SignInActivity.this, "Sign up successful").show();
//                            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
//                            finish();
                        } else {
                            dialog.dismiss();
                            Toasty.warning(SignInActivity.this, "Already Signed Up!!").show();
                            //Toast.makeText(SignUpActivity.this, response.body().getMassage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {

                        dialog.dismiss();
                        Toasty.warning(SignInActivity.this, "Data Not Found").show();
                    }
                });
    }
}