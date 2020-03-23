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

public class KonsultasiHarianAdapter extends RecyclerView.Adapter<KonsultasiHarianAdapter.KonsultasiHarianViewHolder> {
    private List<KonsultasiHarian> konsultasiHarianList;
    private Context ctx;

    public KonsultasiHarianAdapter(List<KonsultasiHarian> konsultasiHarianList, Context context) {
        this.konsultasiHarianList = konsultasiHarianList;
        this.ctx = context;
    }

    @NonNull
    @Override
    public KonsultasiHarianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.konsultasi_harian,parent,false);
        return new KonsultasiHarianAdapter.KonsultasiHarianViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KonsultasiHarianAdapter.KonsultasiHarianViewHolder holder, int position) {
        KonsultasiHarian konsultasiHarian = konsultasiHarianList.get(position);
        holder.hariaktif.setText(konsultasiHarian.getHari());
        holder.jamawal.setText(konsultasiHarian.getJam_awal());
        holder.jamakhir.setText(konsultasiHarian.getJam_akhir());

    }

    @Override
    public int getItemCount() {
        return konsultasiHarianList.size();
    }

    public class KonsultasiHarianViewHolder extends RecyclerView.ViewHolder {
        TextView hariaktif,jamawal,jamakhir;
        public KonsultasiHarianViewHolder(@NonNull View itemView) {
            super(itemView);
            hariaktif = itemView.findViewById(R.id.hari_aktif);
            jamawal = itemView.findViewById(R.id.jam_awal_aktif);
            jamakhir = itemView.findViewById(R.id.jam_akhir_aktif);
        }
    }
}
