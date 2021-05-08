package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.departmentinfos.Models.Faculty;
import com.app.departmentinfos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.Holder> {

    ArrayList<Faculty> faculties = new ArrayList<>();
    Context context;

    public FacultyAdapter(ArrayList<Faculty> teachers, Context context) {
        this.faculties = teachers;
        this.context = context;
    }

    @NonNull
    @Override
    public FacultyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.faculty_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyAdapter.Holder holder, int position) {
        holder.textViewName.setText(faculties.get(position).getName());
        holder.textViewContact.setText(faculties.get(position).getContact_no());
        holder.textViewEmail.setText(faculties.get(position).getEmail());

        if (!faculties.get(position).getDegree2().isEmpty()&& !faculties.get(position).getDegree3().isEmpty()){
            holder.textViewDegree.setText(faculties.get(position).getDegree1()+
                    ","+faculties.get(position).getDegree2()+","+faculties.get(position).getDegree3());
        }else if (!faculties.get(position).getDegree2().isEmpty()){
            holder.textViewDegree.setText(faculties.get(position).getDegree1()+
                    ","+faculties.get(position).getDegree2());
        }else if(!faculties.get(position).getDegree3().isEmpty()){
            holder.textViewDegree.setText(faculties.get(position).getDegree1()+
                    ","+faculties.get(position).getDegree3());
        }else{
            holder.textViewDegree.setText(faculties.get(position).getDegree1());
        }
        Picasso.get().load("https://myfinalproject24.com/admin/img/"+faculties.get(position).getImage()).into(holder.imageViewFaculty);
        holder.textViewDesignation.setText(faculties.get(position).getDesignation());
        holder.cardViewFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ faculties.get(position).getContact_no()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewContact,textViewEmail, textViewDesignation, textViewDegree;
        ImageView imageViewFaculty;
        CardView cardViewFaculty;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.contactTv);
            textViewName = itemView.findViewById(R.id.nameTv);
            textViewEmail = itemView.findViewById(R.id.emailTv);
            textViewDesignation = itemView.findViewById(R.id.designationTv);
            textViewDegree = itemView.findViewById(R.id.degreeTv);
            imageViewFaculty = itemView.findViewById(R.id.facultyIv);
            cardViewFaculty = itemView.findViewById(R.id.facultyCard);
        }
    }
}
