package com.vn.adssdk.facebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sdk.ads.AdSDK;
import com.vn.adssdk.R;
import com.vn.adssdk.google.GoogleInterstitialActivity;
import com.vn.adssdk.google.GoogleInterstitialSupportActivity;

import kotlin.Unit;

public class FbInterstitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_interstitial);

        findViewById(R.id.loadInterstitial).setOnClickListener(v -> {
            /*AdManager.loadFbInterstitial(this);*/
            startActivity(new Intent(FbInterstitialActivity.this, FbInterstitialSupportActivity.class));
        });

        findViewById(R.id.loadInterstitialAndShow).setOnClickListener(v -> {
            /*AdManager.loadFbInterstitial(this, () -> {
                AdManager.showFbInterstitial();
                return Unit.INSTANCE;
            });*/
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}