package com.example.psikologku_psikolog.PsikologFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.psikologku_psikolog.MainActivity;
import com.example.psikologku_psikolog.R;
import com.example.psikologku_psikolog.midtrans_demo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPsikologFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_psikolog_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_psikolog);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PsikologHomeFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.logout:
                intent = new Intent(MainPsikologFragment.this, MainActivity.class);
                finish();
                SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear().commit();
                startActivity(intent);
                break;
            case R.id.midtrans_demo:
                intent = new Intent(MainPsikologFragment.this, midtrans_demo.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new PsikologHomeFragment();
                            break;
                        case R.id.nav_paket:
                            selectedFragment = new PaketKonsultasiFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new PsikologProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };
}
