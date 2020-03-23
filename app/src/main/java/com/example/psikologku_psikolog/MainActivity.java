package com.example.psikologku_psikolog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psikologku_psikolog.PsikologFragments.MainPsikologFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvGotoRegister;
    DatabaseReference ref;
    EditText etEmail,etPassword;
    Boolean trueUser ;
    List<String> email,password,nama;
    Context ctx;
    String error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.bLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvGotoRegister = findViewById(R.id.tvRegister);
        trueUser = false;
        error = "";
        ref = FirebaseDatabase.getInstance().getReference().child("Psikolog");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String em = ds.child("email").getValue(String.class);
                            String ps = ds.child("password").getValue(String.class);
                            String nm = ds.child("nama").getValue(String.class);
                            String id = ds.getKey();
                            if(em.equals(etEmail.getText().toString()) && ps.equals(etPassword.getText().toString()))
                            {
                                trueUser = true;
                                SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("username",nm);
                                edit.putString("id",id);
                                Log.d("username",nm);
                                edit.commit();

                            }
                        }
                        if(trueUser == true)
                        {
                            Intent userIntent = new Intent(MainActivity.this, MainPsikologFragment.class);
                            finish();
                            startActivity(userIntent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Email atau password ada yang salah",Toast.LENGTH_SHORT).show();
                        }
                        trueUser = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this,databaseError.getDetails(),Toast.LENGTH_SHORT);
                    }
                });
            }
        });
        tvGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                finish();
                startActivity(registerIntent);
            }
        });
    }
}
