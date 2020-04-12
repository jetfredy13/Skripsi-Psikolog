package com.example.psikologku_psikolog.PsikologFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.psikologku_psikolog.MainActivity;
import com.example.psikologku_psikolog.R;
import com.example.psikologku_psikolog.SDKConfig;
import com.example.psikologku_psikolog.midtrans_demo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPsikologFragment extends AppCompatActivity implements TransactionFinishedCallback{

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
        initMidtransSDK();
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
            case R.id.midtrans:
                //actionMidtrans();
                intent = new Intent(MainPsikologFragment.this, midtrans_demo.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMidtransSDK()
    {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(SDKConfig.MERCHANT_BASE_CHECKOUT_URL)
                .setClientKey(SDKConfig.MERCHANT_CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .buildSDK();
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
                        case R.id.nav_konseling:
                            selectedFragment = new PsikologListKonselingFragment();
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

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        if(transactionResult.getResponse() != null)
        {
            switch (transactionResult.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this,"Transaction Finished :"+ transactionResult.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this,"Transaction Pending ID : "+ transactionResult.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this,"Transaction Failed ID : "+ transactionResult.getResponse().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case TransactionResult.STATUS_INVALID:
                    Toast.makeText(this,"Transaction Invalid : "+ transactionResult.getResponse().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            transactionResult.getResponse().getValidationMessages();
        }else if(transactionResult.isTransactionCanceled()){
            Toast.makeText(this,"Transaction Canceled",Toast.LENGTH_SHORT).show();
        }else{
            if(transactionResult.getStatus().equalsIgnoreCase(TransactionResult.STATUS_FAILED))
            {
                Toast.makeText(this,"Transaction Invalid",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Transaction Finished with failure",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
