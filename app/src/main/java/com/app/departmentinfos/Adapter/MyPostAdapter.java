package com.app.departmentinfos.Adapter;

import android.app.Activity;
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

import com.app.departmentinfos.Activity.ForumDetailActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.Holder> {
    ArrayList<Forum> forums = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String code="";

    public MyPostAdapter(ArrayList<Forum> forums, Context context) {
        this.forums = forums;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
    }

    @NonNull
    @Override
    public MyPostAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPostAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.forum_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.Holder holder, int position) {
        holder.textViewDescription.setText(forums.get(position).getDescription());
        holder.textViewTitle.setText(forums.get(position).getUser_name());
        holder.textViewDate.setText(forums.get(position).getDesignation().isEmpty()?forums.get(position).getDate():forums.get(position).getDesignation()+"\n"+forums.get(position).getDate());
        holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteForums(forums.get(position).getForumCode(),position);
            }
        });

        if (!forums.get(position).getUserImage().isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+forums.get(position).getUserImage()).into(holder.imageViewUser);
        }else{
            holder.imageViewUser.setImageResource(R.drawable.student);
        }
        holder.cardViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ForumDetailActivity.class).putExtra("forum", forums.get(position)));
                ((Activity)context).finish();
            }
        });
    }
    private void deleteForums(String assignmentCode,int position) {
        ServerCalling.deleteForum(assignmentCode, new Callback<Forum>() {
            @Override
            public void onResponse(Call<Forum> call, Response<Forum> response) {
                if (response.body().getValue().equalsIgnoreCase("success")){
                    //  dialog.dismiss();
                    //new AllAssignmentsFragment().getAssignmentByCode();
                    forums.remove(position);
                    notifyDataSetChanged();
                    Toasty.success(context, "Forum deleted successfully").show();
                }
                else{
                    // dialog.dismiss();
                    Toasty.warning(context,"Unable to delete").show();

                }
            }

            @Override
            public void onFailure(Call<Forum> call, Throwable t) {
                Toasty.warning(context, "Data not found").show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewDate, textViewDelete;
        ImageView imageViewUser;
        CardView cardViewForum;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            textViewDescription = itemView.findViewById(R.id.descTv);
            imageViewUser = itemView.findViewById(R.id.userIv);
            cardViewForum = itemView.findViewById(R.id.forumCard);
            textViewDelete = itemView.findViewById(R.id.deleteTv);
            textViewDelete.setVisibility(View.VISIBLE);
        }
    }
}
