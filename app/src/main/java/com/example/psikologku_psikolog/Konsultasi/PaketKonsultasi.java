package com.example.psikologku_psikolog.Konsultasi;

public class PaketKonsultasi {
    String harga,nama_paket,jumlah_sesi,psikolog_id;
    public String getPsikolog_id() {
        return psikolog_id;
    }
    public void setPsikolog_id(String psikolog_id) {
        this.psikolog_id = psikolog_id;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getJumlah_sesi() {
        return jumlah_sesi;
    }

    public void setJumlah_sesi(String jumlah_sesi) {
        this.jumlah_sesi = jumlah_sesi;
    }
}
