package com.example.psikologku_psikolog.PsikologFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.psikologku_psikolog.R;
import com.example.psikologku_psikolog.SesiKonseling;
import com.example.psikologku_psikolog.User;
import com.example.psikologku_psikolog.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PsikologListKonselingFragment extends Fragment {
    private RecyclerView rec_view_list_konseling;
    private UserAdapter userAdapter;
    private SesiKonseling sesiKonseling;
    private List<SesiKonseling> listSesiKonseling;
    private List<User> listUser;
    private User user;
    DatabaseReference ref;
    SharedPreferences sp;
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
        View v = inflater.inflate(R.layout.fragment_psikolog_list_konseling, container, false);
        rec_view_list_konseling = v.findViewById(R.id.rec_view_list_user_chat);
        rec_view_list_konseling.setHasFixedSize(true);
        rec_view_list_konseling.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadUserKonseling();
        return v;
    }

    private void loadUserKonseling()
    {
        sp = getContext().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        listUser = new ArrayList<>();
        listSesiKonseling = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSesiKonseling.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    sesiKonseling = ds.getValue(SesiKonseling.class);
                    if(sesiKonseling.getId_psikolog().equals(id))
                    {
                        listSesiKonseling.add(sesiKonseling);
                    }
                    ref = FirebaseDatabase.getInstance().getReference("User");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listUser.clear();
                            for(DataSnapshot ds: dataSnapshot.getChildren())
                            {
                                if(listSesiKonseling.size()>0)
                                {
                                    for(int i=0 ; i<listSesiKonseling.size() ;i++)
                                    {
                                        if(ds.getKey().equals(listSesiKonseling.get(i).getId_user()))
                                        {
                                            User user = ds.getValue(User.class);
                                            user.setId(listSesiKonseling.get(i).getId_user());
                                            listUser.add(user);
                                        }
                                    }
                                }
                                userAdapter = new UserAdapter(listUser,getContext());
                                rec_view_list_konseling.setAdapter(userAdapter);
                                userAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
