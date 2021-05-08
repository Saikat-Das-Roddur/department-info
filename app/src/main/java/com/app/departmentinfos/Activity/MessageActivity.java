package com.app.departmentinfos.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.SenderMessageAdapter;
import com.app.departmentinfos.Adapter.MessageRequestAdapter;
import com.app.departmentinfos.Adapter.StudentListAdapter;
import com.app.departmentinfos.Adapter.TeacherListAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    Dialog dialog;
    RecyclerView recyclerViewReceiver, recyclerViewSender, recyclerViewDialog;
    EditText editTextSearch;
    RecyclerView.LayoutManager layoutManagerSender, layoutManagerReceiver, layoutManagerDialog;
    SenderMessageAdapter messageAdapter;
    AlertDialog builder;
    AlertDialog alertDialog;
    MessageRequestAdapter messageRequestAdapter;
    TeacherListAdapter teacherListAdapter;
    StudentListAdapter studentListAdapter;
    ConnectionDetector connectionDetector;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<Teacher> teachers = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String TAG = getClass().getSimpleName(), code = "", department = "", type = "", name = "", receiverName = "", receiverCode = "", image = "", section = "", designation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerViewSender = findViewById(R.id.senderRecyclerView);
        floatingActionButton = findViewById(R.id.floatingBtn);
        //floatingActionButton.setVisibility(View.GONE);
        recyclerViewReceiver = findViewById(R.id.receiverRecyclerView);
        connectionDetector = new ConnectionDetector(this);
        layoutManagerSender = new LinearLayoutManager(this);
        layoutManagerReceiver = new LinearLayoutManager(this);
        recyclerViewReceiver.setLayoutManager(layoutManagerReceiver);
        recyclerViewSender.setLayoutManager(layoutManagerSender);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        section = sharedPreferences.getString(Config.SECTION, "");
        type = sharedPreferences.getString(Config.TYPE, "");
        name = sharedPreferences.getString(Config.USER_NAME, "");
        image = sharedPreferences.getString(Config.IMAGE, "");
        designation = sharedPreferences.getString(Config.DESIGNATION, "");
//        if (connectionDetector.isConnectingToInternet()) {
//            getAcceptedUser();
////            if (type.equalsIgnoreCase("student")) {
////                editTextSearch.setHint("Search for teachers");
////                getTeacher();
////            } else if (type.equalsIgnoreCase("teacher")) {
////                editTextSearch.setHint("Search for students");
////                getStudent();
////            }
//
//        } else
//            new AlertDialog.Builder(MessageActivity.this)
//                    .setTitle("Internet Connection Err!!")
//                    .setMessage("Internet not available, Check your internet connectivity and try again")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }).create().show();

//        editTextSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // arrayAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (type.equalsIgnoreCase("teacher")){
//                    filterStudent(s.toString());
//                }else
//                    filterTeacher(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    showUserDialog();
                }else
                    new AlertDialog.Builder(MessageActivity.this)
                            .setTitle("Internet Connection Err!!")
                            .setMessage("Internet not available, Check your internet connectivity and try again")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
            }
        });
        if (connectionDetector.isConnectingToInternet()) {
            //getRequestedUser();
            getAcceptedUser();
        }else
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

    private void showUserDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_search);
        dialog.setCancelable(true);
        Button buttonCancel;
        editTextSearch = dialog.findViewById(R.id.searchEt);
        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        recyclerViewDialog = dialog.findViewById(R.id.recyclerViewDialog);
        layoutManagerDialog = new LinearLayoutManager(this);
        recyclerViewDialog.setLayoutManager(layoutManagerDialog);
        if (type.equalsIgnoreCase("student"))
            getTeacher();
        else if (type.equalsIgnoreCase("teacher"))
            getStudent();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (type.equalsIgnoreCase("teacher")){
                    filterStudent(s.toString());
                }else
                    filterTeacher(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getStudent() {
        //  users.clear();
        ServerCalling.getStudents(department, new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.body() != null) {

                    studentListAdapter = new StudentListAdapter((ArrayList<Student>) response.body(), MessageActivity.this);
                    recyclerViewDialog.setAdapter(studentListAdapter);
                    studentListAdapter.notifyDataSetChanged();
                    students = (ArrayList<Student>) response.body();
//                    for (int i = 0; i < response.body().size(); i++) {
//                        users.add( response.body().get(i).getCode()+"\n"+response.body().get(i).getName()+"\n"+
//                                response.body().get(i).getSection());
//                        receiverName = response.body().get(i).getName();
//                        receiverCode = response.body().get(i).getCode();
//                        section = response.body().get(i).getSection();
//                        //image = response.body().get(i).getFile();
//                    }

                }
//                LayoutInflater inflater = getLayoutInflater();
//
//                View dialog = inflater.inflate(R.layout.dialog_search, null);
//
//                builder = new AlertDialog.Builder(MessageActivity.this).create();
//               // alertDialog = builder.show();
//                builder.setTitle("Student List");
//                builder.setIcon(R.drawable.student);
//                builder.setView(dialog);
//                builder.show();
//                builder.setView(dialog)
//                        .setTitle("Student List")
//                        .setIcon(R.drawable.student)
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                getAcceptedUser();
//                               // dialog.dismiss();
//                            }
//                        })
//                        .show();
//                EditText editTextSearch  = dialog.findViewById(R.id.searchEt);
//                ListView listViewUser  = dialog.findViewById(R.id.userLv);
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MessageActivity.this, android.R.layout.simple_list_item_1,users.toArray(new String[0]));
//                listViewUser.setAdapter(arrayAdapter);
//               // editTextSearch.setGravity(Gravity.TOP);
//                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //builder.dismiss();
//                        //Toasty.warning(MessageActivity.this,designation).show();
//                        //startActivity(new Intent(MessageActivity.this, UserMessagesActivity.class).putExtra("messages", users.get(position)));
//                        sendMessageRequest(response.body().get(position).getCode(),response.body().get(position).getName(),response.body().get(position).getFile(),response.body().get(position).getSection(),designation);
//                       // startActivity(new Intent(MessageActivity.this, UserMessagesActivity.class));
//                    }
//                });
//                editTextSearch.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                       // arrayAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        arrayAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });


//                        .setItems(users.toArray(new String[0]), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // textViewBranch.setText(branches.get(i));
                //  sendMessageRequest(response.body().get(i).getCode(),response.body().get(i).getName(),response.body().get(i).getFile());
//                    }
//                }).show();
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {

            }
        });
    }

    private void sendMessageRequest(String receiverCode, String receiverName, String receiverImage, String section, String designation) {
        // String[]ar=s.split("\n");
        //Toasty.warning(this,ar[1]).show();
        ServerCalling.addMessageUser(code, name, image, receiverCode, receiverName, receiverImage, section, designation, "accept", new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    //Toasty.success(MessageActivity.this,"Request sent successfully").show();
                    builder.dismiss();
                    startActivity(new Intent(MessageActivity.this, UserMessagesActivity.class).putExtra("messages", response.body()));

//                    alertDialog.dismiss();
                    // getAcceptedUser();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                builder.dismiss();
                Toasty.warning(MessageActivity.this, "Unsuccessful request").show();

            }
        });
    }

    private void getTeacher() {
        users.clear();
        ServerCalling.getTeachers(department, new Callback<List<Teacher>>() {
            @Override
            public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                if (response.body() != null) {
                    Collections.reverse(response.body());
                    teacherListAdapter = new TeacherListAdapter((ArrayList<Teacher>) response.body(), MessageActivity.this);
                    recyclerViewDialog.setAdapter(teacherListAdapter);
                    teacherListAdapter.notifyDataSetChanged();
                    teachers = (ArrayList<Teacher>) response.body();
//                    messageRequestAdapter = new MessageRequestAdapter((ArrayList<Message>) response.body(),MessageActivity.this);
//                    recyclerViewSender.setAdapter(messageRequestAdapter);
//                    messageRequestAdapter.notifyDataSetChanged();
//                    for (int i = 0; i < response.body().size(); i++) {
//                        users.add(response.body().get(i).getName()+"\n"+response.body().get(i).getDesignation());
//                    }
                }
                //LayoutInflater inflater = getLayoutInflater();

//                View dialog = inflater.inflate(R.layout.dialog_search, null);
//                builder = new AlertDialog.Builder(MessageActivity.this).create();
//               // alertDialog = builder.show();
//                builder.setView(dialog);
//                builder.setTitle("Teacher List");
//                builder.setIcon(R.drawable.student);
//                builder.show();
////                builder.setView(dialog)
////                        .setTitle("Teacher List")
////                        .setIcon(R.drawable.student)
////                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                getAcceptedUser();
////                                //dialog.dismiss();
////                            }
////                        })
////                        .show();
//                EditText editTextSearch  = dialog.findViewById(R.id.searchEt);
//                editTextSearch.setHint("Search for teachers");
//                ListView listViewUser  = dialog.findViewById(R.id.userLv);
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MessageActivity.this, android.R.layout.simple_list_item_1,users.toArray(new String[0]));
//                listViewUser.setAdapter(arrayAdapter);
//                // editTextSearch.setGravity(Gravity.TOP);
//                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        builder.dismiss();
//                        //Toasty.warning(MessageActivity.this,response.body().get(position).getDesignation()).show();
//                        sendMessageRequest(response.body().get(position).getCode(),response.body().get(position).getName(),response.body().get(position).getFile(),section,response.body().get(position).getDesignation());
//                    }
//                });
//                editTextSearch.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        arrayAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });

            }

            @Override
            public void onFailure(Call<List<Teacher>> call, Throwable t) {

            }
        });

    }

    private void getAcceptedUser() {
        //Toasty.warning(this,"Now at Accept").show();
        ServerCalling.getAcceptedUser(code, new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.body() != null) {
                    Collections.reverse(response.body());
                    messageRequestAdapter = new MessageRequestAdapter((ArrayList<Message>) response.body(), MessageActivity.this);
                    recyclerViewSender.setAdapter(messageRequestAdapter);
                    messageRequestAdapter.notifyDataSetChanged();
//                    Log.d(TAG, "onResponse: "+response.body().get(0));
                } else {
                    //getAcceptedUser();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toasty.warning(MessageActivity.this, "Data Not Found").show();
            }
        });
    }

    private void getRequestedUser() {
        // Toasty.warning(this,"Now at Request").show();
        ServerCalling.getPendingUser(code, new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.body() != null) {
                    messageRequestAdapter = new MessageRequestAdapter((ArrayList<Message>) response.body(), MessageActivity.this);
                    recyclerViewReceiver.setAdapter(messageRequestAdapter);
                    messageRequestAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toasty.warning(MessageActivity.this, "Data Not Found").show();
            }
        });
    }

    public void goBack(View view) {
        if (type.equalsIgnoreCase("student")) {
            startActivity(new Intent(this, StudentActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, TeacherActivity.class));
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
    public void filterTeacher(String text){
        if (teachers!=null){
            ArrayList<Teacher> temp = new ArrayList<>();
            for (Teacher teacher: teachers){
                if (teacher.getName().toLowerCase().contains(text.toLowerCase())||
                        teacher.getCode().toLowerCase().contains(text.toLowerCase()))
                    temp.add(teacher);
            }
            teacherListAdapter.updateList(temp);
        }

    }

    public void filterStudent(String text){
        if (students!=null){
            ArrayList<Student> temp = new ArrayList<>();
            for (Student student: students){
                if (student.getName().toLowerCase().contains(text.toLowerCase())||
                        student.getCode().toLowerCase().contains(text.toLowerCase()))
                    temp.add(student);
            }
            studentListAdapter.updateList(temp);
        }

    }
}