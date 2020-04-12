package com.example.psikologku_psikolog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> listUser;
    private Context context;

    public UserAdapter(List<User> listUser, Context ctx) {
        this.listUser = listUser;
        this.context = ctx;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_list,parent,false);
        return new UserAdapter.UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
       final User user = listUser.get(position);
        holder.username.setText(user.getNama());
        /*
        if(user.getImageUrl().equals(""))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(user.getImageUrl()).into(holder.profile_image);
        }*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailUser.class);
                intent.putExtra("User",user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_image;
        public TextView username,bidang;
        public UserViewHolder(View itemView){
            super(itemView);
            profile_image = itemView.findViewById(R.id.foto_user);
            username = itemView.findViewById(R.id.nama_user);
            //bidang = itemView.findViewById(R.id.nam);
        }
    }
}
