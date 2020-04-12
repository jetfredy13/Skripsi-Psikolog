package com.example.psikologku_psikolog.PsikologFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.psikologku_psikolog.CreatePaket;
import com.example.psikologku_psikolog.Konsultasi.PaketKonsultasi;
import com.example.psikologku_psikolog.Konsultasi.PaketKonsultasiAdapter;
import com.example.psikologku_psikolog.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;


public class PaketKonsultasiFragment extends Fragment {


    DatabaseReference ref;
    RecyclerView rec_view_paket;
    CollectionReference collectionReference;
    FloatingActionButton fab_paket;
    private List<PaketKonsultasi> list_paket;
    private PaketKonsultasiAdapter paketAdapter;
    SharedPreferences sp;
    public PaketKonsultasiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_paket_konsultasi, container, false);
        rec_view_paket = view.findViewById(R.id.rec_view_paket);
        rec_view_paket.setHasFixedSize(true);
        rec_view_paket.setLayoutManager(new LinearLayoutManager(getContext()));
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        fab_paket = view.findViewById(R.id.fab_add_paket);
        fab_paket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePaket.class);
                startActivity(intent);
            }
        });
        /*

         */
        loadPaket();
        return view;
    }
    private void loadPaket()
    {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        list_paket = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Psikolog").child(id).child("Paket Konseling");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        PaketKonsultasi konsultasi = snap.getValue(PaketKonsultasi.class);
                        list_paket.add(konsultasi);
                    }
                    paketAdapter = new PaketKonsultasiAdapter(list_paket,getContext());
                    rec_view_paket.setAdapter(paketAdapter);
                    paketAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
        Query query = FirebaseDatabase.getInstance().getReference("Psikolog").child(id).child("Paket Konsultasi").orderByChild("psikolog_id").equalTo(id);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_paket.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        PaketKonsultasi konsultasi = snap.getValue(PaketKonsultasi.class);
                        list_paket.add(konsultasi);
                    }
                    paketAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);
        paketAdapter = new PaketKonsultasiAdapter(list_paket,getContext());
        rec_view_paket.setAdapter(paketAdapter);
         */
    }
}
