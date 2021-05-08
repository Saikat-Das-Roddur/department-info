package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import com.app.departmentinfos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SenderMessageAdapter extends RecyclerView.Adapter<SenderMessageAdapter.Holder> {
    ArrayList<Message> messages = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;

    public SenderMessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public SenderMessageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.receiver_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SenderMessageAdapter.Holder holder, int position) {
        holder.textViewName.setText(messages.get(position).getReceiverName());
       // holder.textViewCode.setText(messages.get(position).getReceiverCode());
        //holder.textViewStatus.setText(messages.get(position).getStatus());
        Picasso.get().load("https://myfinalproject24.com/android/img/"+messages.get(position).getReceiverFile()).into(holder.imageViewUser);

        holder.cardViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    context.startActivity(new Intent(context, UserMessagesActivity.class).putExtra("messages", messages.get(position)));
                ((MessageActivity)context).finish();
                   // Toasty.success(context,messages.get(position).getSenderFile()).show();

            }
        });

    }

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
            imageViewUser = itemView.findViewById(R.id.userIv);
            cardViewMessage = itemView.findViewById(R.id.messageCard);
        }
    }
}
