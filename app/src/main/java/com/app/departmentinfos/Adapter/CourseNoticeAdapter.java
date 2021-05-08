package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CourseNoticeAdapter extends RecyclerView.Adapter<CourseNoticeAdapter.Holder> {
    ArrayList<Notice> notices = new ArrayList<>();
    Context context;

    public CourseNoticeAdapter(ArrayList<Notice> notices, Context context) {
        this.notices = notices;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseNoticeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.notices_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseNoticeAdapter.Holder holder, int position) {
        if (!notices.get(position).getImage().isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+notices.get(position).getImage()).into(holder.imageViewUser);
        }else{
            holder.imageViewUser.setImageResource(R.drawable.bell);
        }
        holder.textViewDesc.setText(notices.get(position).getDescription());
        holder.textViewTitle.setText(notices.get(position).getSection());
        holder.textViewDate.setText(notices.get(position).getPostingDate());
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public void updateList(ArrayList<Notice> notices) {
        this.notices = notices;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDesc,textViewDate;
        ImageView imageViewUser;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            imageViewUser = itemView.findViewById(R.id.userIv);
        }
    }
}
