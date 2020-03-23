package com.example.psikologku_psikolog.Konsultasi;

public class KonsultasiHarian {
    private String hari,jam_awal,jam_akhir,tanggal,psikolog_id;

    public String getPsikolog_id() {
        return psikolog_id;
    }

    public void setPsikolog_id(String psikolog_id) {
        this.psikolog_id = psikolog_id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam_awal() {
        return jam_awal;
    }

    public void setJam_awal(String jam_awal) {
        this.jam_awal = jam_awal;
    }

    public String getJam_akhir() {
        return jam_akhir;
    }

    public void setJam_akhir(String jam_akhir) {
        this.jam_akhir = jam_akhir;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
