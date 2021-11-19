package com.vn.adssdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.ads.AdSDK;
import com.vn.adssdk.facebook.FbBannerActivity;
import com.vn.adssdk.facebook.FbInterstitialActivity;
import com.vn.adssdk.facebook.FbNativeActivity;
import com.vn.adssdk.facebook.FbRewardedActivity;
import com.vn.adssdk.google.GoogleBannerActivity;
import com.vn.adssdk.google.GoogleInterstitialActivity;
import com.vn.adssdk.google.GoogleNativeActivity;
import com.vn.adssdk.google.GoogleRewardedActivity;
import com.vn.adssdk.mix.FbThenGGBannerActivity;
import com.vn.adssdk.mix.FbThenGGInterstitialActivity;
import com.vn.adssdk.mix.FbThenGGNativeActivity;
import com.vn.adssdk.mix.FbThenGGRewardedActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private com.vn.adssdk.databinding.ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = com.vn.adssdk.databinding.ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        findViewById(R.id.btnGoogleBanner).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GoogleBannerActivity.class));
        });

        findViewById(R.id.btnGoogleInterstitial).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GoogleInterstitialActivity.class));
        });

        findViewById(R.id.btnGoogleNative).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GoogleNativeActivity.class));
        });

        findViewById(R.id.btnGoogleRewarded).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GoogleRewardedActivity.class));
        });







        findViewById(R.id.btnFacebookBanner).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbBannerActivity.class));
        });

        findViewById(R.id.btnFacebookInterstitial).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbInterstitialActivity.class));
        });

        findViewById(R.id.btnFacebookNative).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbNativeActivity.class));
        });

        findViewById(R.id.btnFacebookRewarded).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbRewardedActivity.class));
        });



        findViewById(R.id.btnBanner).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbThenGGBannerActivity.class));
        });

        findViewById(R.id.btnNative).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbThenGGNativeActivity.class));
        });

        findViewById(R.id.btnRewarded).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbThenGGRewardedActivity.class));
        });

        findViewById(R.id.btnInterstitial).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FbThenGGInterstitialActivity.class));
        });
    }
}