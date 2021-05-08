package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.R;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.Holder> {
    SharedPreferences sharedPreferences;
    ArrayList<Message> messages = new ArrayList<>();
    Context context;
    LinearLayout.LayoutParams paramsLeft,paramsRight;
    String code="";

    public MessagesAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        paramsRight = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLeft = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sharedPreferences=context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        code=sharedPreferences.getString(Config.CODE,"");
    }

    @NonNull
    @Override
    public MessagesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.messages_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.Holder holder, int position) {

        if (messages.get(position).getSenderCode().equalsIgnoreCase(code)){
            holder.msgLeftLinearLayout.setVisibility(View.GONE);
            holder.textViewSenderMsg.setText(messages.get(position).getSenderMessage());
            holder.textViewDate.setText(messages.get(position).getDate());
            holder.textViewSenderTime.setText(messages.get(position).getTime());
        }else {
            holder.msgRightLinearLayout.setVisibility(View.GONE);
            holder.textViewReceiverMsg.setText(messages.get(position).getSenderMessage());
            holder.textViewDate.setText(messages.get(position).getDate());
            holder.textViewReceiverTime.setText(messages.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout msgLeftLinearLayout, msgRightLinearLayout;
        TextView textViewSenderMsg, textViewReceiverMsg, textViewSenderTime, textViewReceiverTime, textViewDate;

        public Holder(@NonNull View itemView) {
            super(itemView);
            msgLeftLinearLayout = itemView.findViewById(R.id.chat_left_msg_layout);
            msgRightLinearLayout = itemView.findViewById(R.id.chat_right_msg_text_view);
            textViewSenderMsg = itemView.findViewById(R.id.senderMsgTv);
            textViewReceiverMsg = itemView.findViewById(R.id.receiverMsgTv);
            textViewSenderTime = itemView.findViewById(R.id.senderTimeTv);
            textViewReceiverTime = itemView.findViewById(R.id.receiverTimeTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
        }
    }
}
