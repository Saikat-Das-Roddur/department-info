package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Activity.UserMessagesActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRequestAdapter extends RecyclerView.Adapter<MessageRequestAdapter.Holder> {
    ArrayList<Message> messages=new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String code="",name="",image="",type="";

    public MessageRequestAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        sharedPreferences=context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        code=sharedPreferences.getString(Config.CODE,"");
        type=sharedPreferences.getString(Config.TYPE,"");
        name=sharedPreferences.getString(Config.USER_NAME,"");
        image=sharedPreferences.getString(Config.IMAGE,"");
    }

    @NonNull
    @Override
    public MessageRequestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.receiver_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRequestAdapter.Holder holder, int position) {

       // holder.textViewCode.setText(messages.get(position).getSenderCode());

       // holder.textViewStatus.setText(messages.get(position).getStatus());
        if (messages.get(position).getStatus().equalsIgnoreCase("unread")){
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textViewStatus.setText(messages.get(position).getStatus());
        }

        if (messages.get(position).getSenderCode().equalsIgnoreCase(code)){
            holder.textViewName.setText(messages.get(position).getReceiverName());
            if (type.equalsIgnoreCase("teacher")){
                holder.textViewCode.setText(messages.get(position).getReceiverCode()+"\n"+messages.get(position).getSection());
            }

           // holder.textViewName.setText(messages.get(position).get());
            Picasso.get().load("https://myfinalproject24.com/android/img/"+messages.get(position).getReceiverFile()).into(holder.imageViewUser);
        }else {
            if (type.equalsIgnoreCase("teacher")){
                holder.textViewCode.setText(messages.get(position).getSenderCode());
            }
            holder.textViewName.setText(messages.get(position).getSenderName());
            Picasso.get().load("https://myfinalproject24.com/android/img/"+messages.get(position).getSenderFile()).into(holder.imageViewUser);
        }

      holder.cardViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messages.get(position).getStatus().equalsIgnoreCase("unread"))
                    updateMessageStatus(position);
                context.startActivity(new Intent(context, UserMessagesActivity.class).putExtra("messages", messages.get(position)));
            }
        });

    }

    private void updateMessageStatus( int position) {
        ServerCalling.updateMessageStatus(messages.get(position).getSenderCode(), messages.get(position).getReceiverCode(), "read", new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                    //Toasty.success(context,"Updated").show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

//    private void addMessageUser(int position) {
//        ServerCalling.addMessageUser(code, name,image, messages.get(position).getSenderCode(), messages.get(position).getSenderName(),messages.get(position).getSenderFile(), "accept",
//                new Callback<Message>() {
//                    @Override
//                    public void onResponse(Call<Message> call, Response<Message> response) {
//                        if (response.body().getValue().equalsIgnoreCase("success")){
//                            Toasty.success(context,"SuccessFully Added").show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Message> call, Throwable t) {
//
//                    }
//                });
//        //Toasty.warning(context,"Request send from"+messages.get(position).getSenderCode()).show();
//    }

    @Override
    public int getItemCount() {
        return messages.size();
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
}
