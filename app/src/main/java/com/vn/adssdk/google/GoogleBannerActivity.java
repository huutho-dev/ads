package com.vn.adssdk.google;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.vn.adssdk.R;

public class GoogleBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_banner);

//        AdManager.showGGBanner(this, findViewById(R.id.bannerContainer));
    }
}