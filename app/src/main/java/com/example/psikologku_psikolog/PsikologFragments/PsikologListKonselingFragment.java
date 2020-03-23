package com.example.psikologku_psikolog.PsikologFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psikologku_psikolog.Psikolog;
import com.example.psikologku_psikolog.PsikologAdapter;
import com.example.psikologku_psikolog.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class PsikologListKonselingFragment extends Fragment {
    private RecyclerView rec_view_list_konseling;
    private PsikologAdapter adapter;
    DatabaseReference reference;
    private List<String> uList;
    private List<Psikolog> ListPsikolog;
    SharedPreferences sp;
    String currUser;

    public PsikologListKonselingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_psikolog_list_konseling, container, false);
    }

}
