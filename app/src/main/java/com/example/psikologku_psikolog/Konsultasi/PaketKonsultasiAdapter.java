package com.example.psikologku_psikolog.Konsultasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologku_psikolog.R;

import java.util.List;

public class PaketKonsultasiAdapter extends RecyclerView.Adapter<PaketKonsultasiAdapter.PaketKonsultasiViewHolder> {
    private List<PaketKonsultasi> list_paketkonsultasi;
    private Context context;

    public PaketKonsultasiAdapter(List<PaketKonsultasi> list_paketkonsultasi, Context context) {
        this.list_paketkonsultasi = list_paketkonsultasi;
        this.context = context;
    }

    @NonNull
    @Override
    public PaketKonsultasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paket_konseling,parent,false);
        return new PaketKonsultasiAdapter.PaketKonsultasiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaketKonsultasiViewHolder holder, int position) {
        PaketKonsultasi paket = list_paketkonsultasi.get(position);
        holder.nama_paket.setText(paket.getNama_paket());
        holder.jumlah_sesi.setText("Jumlah Sesi :"+paket.getJumlah_sesi());
        holder.harga.setText("Rp "+paket.getHarga());
    }

    @Override
    public int getItemCount() {
        return list_paketkonsultasi.size();
    }

    public class PaketKonsultasiViewHolder extends RecyclerView.ViewHolder{
      TextView nama_paket,harga,jumlah_sesi;
      public PaketKonsultasiViewHolder(@NonNull View itemView) {
          super(itemView);
          nama_paket = itemView.findViewById(R.id.nama_paket);
          harga = itemView.findViewById(R.id.harga_paket);
          jumlah_sesi = itemView.findViewById(R.id.jumlah_sesi);
      }
  }
}
