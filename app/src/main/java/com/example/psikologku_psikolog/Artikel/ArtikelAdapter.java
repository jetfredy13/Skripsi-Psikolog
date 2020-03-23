package com.example.psikologku_psikolog.Artikel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologku_psikolog.Psikolog;
import com.example.psikologku_psikolog.R;

import java.util.List;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private List<Artikel> list_artikel;
    private List<Psikolog> list_psikolog;
    private Context context;
    public ArtikelAdapter (Context ctx , List<Artikel> listArtikel, List<Psikolog> listPsikolog)
    {
        this.context = ctx;
        this.list_artikel = listArtikel;
        this.list_psikolog = listPsikolog;
    }
    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view,parent,false);
        return new ArtikelAdapter.ArtikelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        final Artikel artikel = list_artikel.get(position);
        final Psikolog psikolog = list_psikolog.get(position);
        holder.judul_artikel.setText(artikel.getJudul());
        holder.isi_artikel.setText(artikel.getIsi_artikel());
        holder.nama.setText(psikolog.getNama());
        holder.bidang.setText(psikolog.getBidang());
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
            isi_artikel = itemView.findViewById(R.id.isi_artikel);
            bidang = itemView.findViewById(R.id.bidang);
        }
    }
}
