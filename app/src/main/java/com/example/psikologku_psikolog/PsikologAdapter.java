package com.example.psikologku_psikolog;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

public class PsikologAdapter extends RecyclerView.Adapter<PsikologAdapter.PsikologViewHolder> {
    private List<Psikolog> user_psikolog;
    private Context context;

    public PsikologAdapter(Context ctx,List<Psikolog> userPsikolog) {
        this.user_psikolog = userPsikolog;
        this.context = ctx;
    }

    public class PsikologViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView username,bidang;
        public PsikologViewHolder(View itemView){
            super(itemView);
            profile_image = itemView.findViewById(R.id.imageView);
            username = itemView.findViewById(R.id.nama_psikolog);
            bidang = itemView.findViewById(R.id.bidang_psikolog);
        }
    }

    @NonNull
    @Override
    public PsikologViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_list,parent,false);
        return new PsikologAdapter.PsikologViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PsikologViewHolder holder, int position) {
        final Psikolog psikolog = user_psikolog.get(position);
        holder.username.setText(psikolog.getNama());
        holder.bidang.setText(psikolog.getBidang());
        if(psikolog.getImage_url().equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(psikolog.getImage_url()).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context,DetailPsikolog.class);
                //intent.putExtra("Psikolog",psikolog);
                //intent.putExtra("id",psikolog.getNama());
                //intent.putExtra("key",psikolog.getEmail());
                //context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user_psikolog.size();
    }
}
