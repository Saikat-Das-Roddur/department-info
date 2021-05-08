package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.R;

import java.util.ArrayList;

public class AdminNoticeAdapter extends RecyclerView.Adapter<AdminNoticeAdapter.Holder> {

    ArrayList<Notice> notices = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String code="", section="", department="";

    public AdminNoticeAdapter(ArrayList<Notice> notices, Context context) {
        this.notices = notices;
        this.context = context;
//        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
//        code = sharedPreferences.getString(Config.CODE,"");
    }

    @NonNull
    @Override
    public AdminNoticeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.notices_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminNoticeAdapter.Holder holder, int position) {
            holder.textViewTitle.setText(notices.get(position).getDept()+"- Office");
            holder.textViewDesc.setText(notices.get(position).getDescription());
            holder.textViewDate.setText(notices.get(position).getPostingDate());


    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDesc,textViewDate;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
        }
    }
}
