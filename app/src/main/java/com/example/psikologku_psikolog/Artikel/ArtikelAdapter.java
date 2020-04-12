package com.example.psikologku_psikolog.Artikel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologku_psikolog.Psikolog;
import com.example.psikologku_psikolog.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private List<Artikel> list_artikel;
    Psikolog psikolog;
    DatabaseReference reference;
    //private List<Psikolog> list_psikolog;
    private Context context;
    public ArtikelAdapter (Context ctx , List<Artikel> listArtikel)
    {
        this.context = ctx;
        this.list_artikel = listArtikel;
        //this.list_psikolog = listPsikolog;
    }
    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view,parent,false);
        return new ArtikelAdapter.ArtikelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtikelViewHolder holder, int position) {
        final Artikel artikel = list_artikel.get(position);
        //final Psikolog psikolog = list_psikolog.get(position);
        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(artikel.getPenulis());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                psikolog = dataSnapshot.getValue(Psikolog.class);
                psikolog.setId(dataSnapshot.getKey());
                holder.judul_artikel.setText(artikel.getJudul());
                holder.nama.setText(psikolog.getNama());
                /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,DetailArtikel.class);
                        intent.putExtra("Artikel",artikel);
                        intent.putExtra("Psikolog",psikolog);
                        context.startActivity(intent);
                    }
                }); */
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_artikel.size();
    }

    public class ArtikelViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView nama,bidang,judul_artikel,isi_artikel;
        public ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_psikolog);
            nama = itemView.findViewById(R.id.nama_psikolog);
            judul_artikel = itemView.findViewById(R.id.judul);
        }
    }
}
