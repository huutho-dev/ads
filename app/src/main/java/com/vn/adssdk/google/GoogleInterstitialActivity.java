package com.vn.adssdk.google;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.vn.adssdk.MainActivity;
import com.vn.adssdk.R;

import kotlin.Unit;

public class GoogleInterstitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_interstitial);

        findViewById(R.id.loadInterstitial).setOnClickListener(v -> {
//            AdManager.loadGGInters(this);
            startActivity(new Intent(GoogleInterstitialActivity.this, GoogleInterstitialSupportActivity.class));
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        AdManager.showGGInters(this);
    }
}