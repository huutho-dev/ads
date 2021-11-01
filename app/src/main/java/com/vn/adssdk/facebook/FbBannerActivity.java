package com.vn.adssdk.facebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;


public class FbBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_banner);

        AdManager.showFbBanner(findViewById(R.id.bannerFbContainer));
    }
}