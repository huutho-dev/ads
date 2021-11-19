package com.vn.adssdk.google;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdk.ads.AdSDK;
import com.sdk.ads.manager.AdManager;
import com.vn.adssdk.R;

public class GoogleBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_banner);

        AdSDK.showBanner("banner1", findViewById(R.id.bannerContainer));
    }
}