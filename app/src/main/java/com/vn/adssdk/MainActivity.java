package com.vn.adssdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.sdk.ads.AdManager;
import com.sdk.ads.facebook.FacebookInterstitial;
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

    String adsJsonConfig = "{\n" +
            "  \"isEnable\" : true,\n" +
            "  \"ads\": {\n" +
            "    \"google\": {\n" +
            "      \"banner\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/6300978111\"\n" +
            "      },\n" +
            "      \"interstitial\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/1033173712\"\n" +
            "      },\n" +
            "      \"rewarded\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/5224354917\"\n" +
            "      },\n" +
            "      \"native\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/2247696110\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"facebook\": {\n" +
            "      \"banner\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233074908888895\"\n" +
            "      },\n" +
            "      \"interstitial\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233084782221241\"\n" +
            "      },\n" +
            "      \"rewarded\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233085135554539\"\n" +
            "      },\n" +
            "      \"native\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233084965554556\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdManager.initialize(this);
        AdManager.setAdsConfig(adsJsonConfig, false);

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