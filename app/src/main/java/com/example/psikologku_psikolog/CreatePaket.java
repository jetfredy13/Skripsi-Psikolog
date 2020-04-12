package com.example.psikologku_psikolog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.psikologku_psikolog.Konsultasi.PaketKonsultasi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePaket extends AppCompatActivity {
    Button btnAdd;
    EditText nama_paket,harga,jumlah_sesi;
    DatabaseReference ref;
    PaketKonsultasi paket;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_paket);
        sp = CreatePaket.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        btnAdd = findViewById(R.id.tambah_paket);
        nama_paket = findViewById(R.id.nama_paket);
        harga = findViewById(R.id.harga_paket);
        jumlah_sesi = findViewById(R.id.jumlah_sesi);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paket = new PaketKonsultasi();
                ref = FirebaseDatabase.getInstance().getReference("Psikolog").child(id).child("Paket Konseling");
                paket.setHarga(harga.getText().toString());
                paket.setNama_paket(nama_paket.getText().toString());
                paket.setJumlah_sesi(jumlah_sesi.getText().toString());
                ref.push().setValue(paket);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
