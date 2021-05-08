package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.MessageActivity;
import com.app.departmentinfos.Activity.UserMessagesActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.Models.Teacher;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.Holder> {
    ArrayList<Teacher> teachers = new ArrayList<>();
    Context context;
    String code="",name="",image="",section="";
    SharedPreferences sharedPreferences;

    public TeacherListAdapter(ArrayList<Teacher> teachers, Context context) {
        this.teachers = teachers;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        name = sharedPreferences.getString(Config.USER_NAME,"");
        image = sharedPreferences.getString(Config.IMAGE,"");
        section = sharedPreferences.getString(Config.SECTION,"");
    }

    @NonNull
    @Override
    public TeacherListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.receiver_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherListAdapter.Holder holder, int position) {
        holder.textViewName.setText(teachers.get(position).getName());
        holder.textViewCode.setText(teachers.get(position).getDesignation());
        if (teachers.get(position).getFile().isEmpty()){
            holder.imageViewUser.setImageResource(R.drawable.teacher_2);
        }else
            Picasso.get().load("https://myfinalproject24.com/android/img/"+teachers.get(position).getFile()).into(holder.imageViewUser);

        holder.cardViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageRequest(teachers.get(position).getCode(),teachers.get(position).getName(),teachers.get(position).getFile(),teachers.get(position).getDesignation());
                //context.startActivity(new Intent(context, UserMessagesActivity.class).putExtra("messages", teachers.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public void updateList(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCode, textViewStatus;
        ImageView imageViewUser;
        CardView cardViewMessage;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.nameTv);
            textViewCode = itemView.findViewById(R.id.codeTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            cardViewMessage = itemView.findViewById(R.id.messageCard);
            imageViewUser = itemView.findViewById(R.id.userIv);
        }
    }

    private void sendMessageRequest(String receiverCode,String receiverName,String receiverImage,String designation) {
        // String[]ar=s.split("\n");
        //Toasty.warning(this,ar[1]).show();
        ServerCalling.addMessageUser(code, name,image, receiverCode,receiverName,receiverImage,section,designation, "accept", new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    //Toasty.success(MessageActivity.this,"Request sent successfully").show();
                   // builder.dismiss();
                    context.startActivity(new Intent(context, UserMessagesActivity.class).putExtra("messages",response.body()));

//                    alertDialog.dismiss();
                    // getAcceptedUser();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toasty.warning(context,"Unsuccessful request").show();

            }
        });
    }

}
