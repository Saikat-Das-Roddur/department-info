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
import com.app.departmentinfos.Activity.ProfileSettingsActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Models.Comments;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    SharedPreferences sharedPreferences;
    ArrayList<Comments> comments = new ArrayList<>();
    Context context;
    String code="", blogger_code = "";

    public CommentAdapter(ArrayList<Comments> comments, Context context) {
        this.comments = comments;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        code = sharedPreferences.getString(Config.CODE,"");
        blogger_code = sharedPreferences.getString(Config.BLOGGER_CODE,"");
    }

    @NonNull
    @Override
    public CommentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.comments_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Holder holder, int position) {
        holder.textViewName.setText(comments.get(position).getUserName());
        holder.textViewDate.setText(comments.get(position).getDate());
        holder.textViewComments.setText(comments.get(position).getComment());
         if (!comments.get(position).getImage().isEmpty()){
            Picasso.get().load("https://myfinalproject24.com/android/img/"+comments.get(position).getImage())
                    .placeholder(R.drawable.student)
                    .into(holder.imageViewUser);
        }else{
            holder.imageViewUser.setImageResource(R.drawable.student);
        }
        if (code.equalsIgnoreCase(blogger_code)){
            holder.imageViewHelpful.setVisibility(View.VISIBLE);
            holder.imageViewNotHelpful.setVisibility(View.VISIBLE);
            holder.imageViewHelpful.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upDateComment(comments.get(position).getId(),"helpful");
                }
            });
            holder.imageViewNotHelpful.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upDateComment(comments.get(position).getId(),"unhelpful");
                }
            });
        }else {
            if (comments.get(position).getStatus().equalsIgnoreCase("helpful")){
                holder.imageViewNotHelpful.setVisibility(View.VISIBLE);
                holder.textViewHelpFul.setVisibility(View.VISIBLE);
                holder.textViewHelpFul.setText(comments.get(position).getStatus());
                holder.imageViewNotHelpful.setImageResource(R.drawable.baseline_check_white_24);
                holder.imageViewNotHelpful.setColorFilter(((Activity)context).getResources().getColor(R.color.colorPrimary));
            }
        }
        holder.cardViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comments.get(position).getCode().equals(code)){
                   // context.startActivity(new Intent(context,ForumDetailActivity.class).putExtra("comment",comments.get(position)));
                  //  ((ForumDetailActivity)context).showCommentDialog();
                   // ((ForumDetailActivity) context).finish();
                }

            }
        });
    }

    private void upDateComment(int id, String status) {
        ServerCalling.updateComment(id, status, new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                if (response.body().getValue().equals("success")) {

                    Toasty.success(context, "Status Updated SuccessFully").show();


                } else {
                    Toasty.warning(context, "Can't Update profile").show();
                }
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                Toasty.warning(context, "Data Not Found").show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDate, textViewComments,textViewHelpFul;
        ImageView imageViewHelpful, imageViewNotHelpful,imageViewUser;
        CardView cardViewComment;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.nameTv);
            textViewComments = itemView.findViewById(R.id.commentTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            textViewHelpFul = itemView.findViewById(R.id.helpfulTv);
            imageViewHelpful = itemView.findViewById(R.id.helpfulIv);
            imageViewNotHelpful = itemView.findViewById(R.id.noHelpfulIv);
            imageViewUser = itemView.findViewById(R.id.userIv);

            cardViewComment = itemView.findViewById(R.id.commentCard);
            imageViewHelpful.setVisibility(View.GONE);
            imageViewNotHelpful.setVisibility(View.GONE);
        }
    }
}
