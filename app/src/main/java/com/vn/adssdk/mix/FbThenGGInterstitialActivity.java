package com.vn.adssdk.mix;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.vn.adssdk.R;

import kotlin.Unit;

public class FbThenGGInterstitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_then_gginterstitial);

        findViewById(R.id.loadInterstitial).setOnClickListener(v -> {
            /*AdManager.loadInterstitial(this);*/
            startActivity(new Intent(
                    FbThenGGInterstitialActivity.this,
                    FbThenGGInterstitialSupportActivity.class));
        });

        findViewById(R.id.loadInterstitialAndShow).setOnClickListener(v -> {
            /*AdManager.loadInterstitial(this, () -> {
                AdManager.showInterstitial(this);
                return Unit.INSTANCE;
            });*/
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* AdManager.showInterstitial(this);*/
    }
}