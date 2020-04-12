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

import com.example.psikologku_psikolog.Artikel.Artikel;
import com.example.psikologku_psikolog.Artikel.ArtikelAdapter;
import com.example.psikologku_psikolog.Artikel.CreateArticle;
import com.example.psikologku_psikolog.Psikolog;
import com.example.psikologku_psikolog.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PsikologHomeFragment extends Fragment {
    FloatingActionButton fab ;
    private RecyclerView rec_view_artikel;
    private ArtikelAdapter artikel_adapter;
    private List<Artikel> list_artikel;
    private List<Psikolog> list_psikolog;
    DatabaseReference ref;
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_psikolog_home,container,false);
        rec_view_artikel = view.findViewById(R.id.rec_view_artikel);
        rec_view_artikel.setHasFixedSize(true);
        rec_view_artikel.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadArtikel();
        fab = view.findViewById(R.id.fab_add_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateArticle.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void loadArtikel()
    {
        list_artikel = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Artikel");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_artikel.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Artikel art = snap.getValue(Artikel.class);
                    /*ref = FirebaseDatabase.getInstance().getReference("Psikolog");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list_psikolog.clear();
                            for(int i =0; i<list_artikel.size() ; i++)
                            {
                                for(DataSnapshot snap : dataSnapshot.getChildren())
                                {
                                    if(snap.getKey().equals(list_artikel.get(i).getPenulis()))
                                    {
                                        Psikolog psikolog = snap.getValue(Psikolog.class);
                                        list_psikolog.add(psikolog);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });*/
                    list_artikel.add(art);
                    artikel_adapter = new ArtikelAdapter(getContext(),list_artikel);
                    rec_view_artikel.setAdapter(artikel_adapter);
                    artikel_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
