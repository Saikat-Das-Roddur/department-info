package com.app.departmentinfos.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.MessagesAdapter;
import com.app.departmentinfos.Adapter.SenderMessageAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMessagesActivity extends AppCompatActivity {
    TextView textViewName;
    MessagesAdapter messageAdapter;
    FloatingActionButton floatingActionButton;
    EditText editTextSendMessage;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ConnectionDetector connectionDetector;
    SharedPreferences sharedPreferences;
    Message message;
    String code = "",TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);
        textViewName = findViewById(R.id.nameTv);
        editTextSendMessage=findViewById(R.id.sendEt);
        floatingActionButton =findViewById(R.id.sendBtn);
        connectionDetector = new ConnectionDetector(this);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
       // ((LinearLayoutManager)layoutManager).setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME,MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        message = getIntent().getParcelableExtra("messages");
        Log.d(TAG, "onCreate: "+message);
        if(!code.equalsIgnoreCase(message.getSenderCode())){
            textViewName.setText(message.getSenderName());
        }else {
            textViewName.setText(message.getReceiverName());
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextSendMessage.getText().toString().isEmpty()){
                    editTextSendMessage.setError("Please type message");
                }else {
                    if (connectionDetector.isConnectingToInternet())
                    sendMessage();
                    else
                        new AlertDialog.Builder(UserMessagesActivity.this)
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
        if (connectionDetector.isConnectingToInternet())
        getMessages();
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

    private void getMessages() {
        if (message!=null){
            ServerCalling.getMessages(code, message.getReceiverCode(), new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.body()!=null){
                        messageAdapter = new MessagesAdapter((ArrayList<Message>) response.body(),UserMessagesActivity.this);
                        recyclerView.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });

        }
       }

    private void sendMessage() {
        if (message!=null){
            ServerCalling.addMessages(code, editTextSendMessage.getText().toString(), message.getReceiverCode().equalsIgnoreCase(code)?message.getSenderCode():message.getReceiverCode(),
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()),new SimpleDateFormat("hh:mm a").format(new Date()), "unread",new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.body().getValue().equals("success")){
                                updateMessageStatus();
                                Toasty.success(UserMessagesActivity.this,"Message sent successfully").show();
                                editTextSendMessage.setText("");
                                getMessages();



                            }else {
                                Toasty.warning(UserMessagesActivity.this,"Message Not Sent").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
//                            Toasty.success(UserMessagesActivity.this,"Message sent successfully").show();
//                            editTextSendMessage.setText("");
//                            getMessages();
//                            updateMessageStatus();
                        }
                    });
        }
    }
    private void updateMessageStatus() {
        ServerCalling.updateMessageStatus(code, message.getReceiverCode().equalsIgnoreCase(code)?message.getSenderCode():message.getReceiverCode(), "unread", new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                    //Toasty.success(UserMessagesActivity.this,"Updated").show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    public void goBack(View view) {
            startActivity(new Intent(UserMessagesActivity.this, MessageActivity.class));
            finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserMessagesActivity.this, MessageActivity.class));
        finish();
    }
}