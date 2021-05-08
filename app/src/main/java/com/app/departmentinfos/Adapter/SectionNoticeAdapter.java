package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SectionNoticeAdapter extends RecyclerView.Adapter<SectionNoticeAdapter.Holder> {
    ArrayList<Notice> notices = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String code = "", section = "", department = "", type = "";

    public SectionNoticeAdapter(ArrayList<Notice> notices, Context context) {
        this.notices = notices;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE, "");
        section = sharedPreferences.getString(Config.SECTION, "");
        department = sharedPreferences.getString(Config.DEPARTMENT, "");
        type = sharedPreferences.getString(Config.TYPE, "");
    }

    @NonNull
    @Override
    public SectionNoticeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.notices_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionNoticeAdapter.Holder holder, int position) {
        if (!notices.get(position).getImage().isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+notices.get(position).getImage()).into(holder.imageViewUser);
        }else{
            holder.imageViewUser.setImageResource(R.drawable.bell);
        }
        holder.textViewDesc.setText(notices.get(position).getDescription());
        if (type.equalsIgnoreCase("teacher"))
            holder.textViewUserName.setText(notices.get(position).getSection());
        else
            holder.textViewUserName.setText(notices.get(position).getUserName());
        holder.textViewDate.setText(notices.get(position).getPostingDate());
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewUserName, textViewDesc, textViewDate;
        ImageView imageViewUser;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.titleTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            imageViewUser = itemView.findViewById(R.id.userIv);
        }
    }
}
