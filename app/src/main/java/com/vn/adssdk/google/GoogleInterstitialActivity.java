package com.vn.adssdk.google;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.ads.AdManager;
import com.vn.adssdk.MainActivity;
import com.vn.adssdk.R;

import kotlin.Unit;

public class GoogleInterstitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_interstitial);

        findViewById(R.id.loadInterstitial).setOnClickListener(v -> {
            AdManager.loadGGInterstitial(this);
            startActivity(new Intent(GoogleInterstitialActivity.this, GoogleInterstitialSupportActivity.class));
        });

        findViewById(R.id.loadInterstitialAndShow).setOnClickListener(v -> {
            AdManager.loadGGInterstitial(
                    this,
                    interstitialAd -> {
                        AdManager.showGGInterstitial(this);
                        return Unit.INSTANCE;
                    },
                    loadAdError -> Unit.INSTANCE
            );
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AdManager.showGGInterstitial(this);
    }
}