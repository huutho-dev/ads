package com.vn.adssdk.google;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.nativead.NativeAd;
import com.sdk.ads.AdSDK;
import com.sdk.ads.manager.nativeAd.NativeAdListener;
import com.vn.adssdk.R;

public class GoogleNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_native);


        AdSDK.showNative("native1", findViewById(R.id.templateView), false, new NativeAdListener() {
            @Override
            public void onNativeImpression() {

            }

            @Override
            public void onNativeClicked() {

            }

            @Override
            public void onNativeLoaded() {

            }

            @Override
            public void onNativeOpened() {

            }

            @Override
            public void onNativeClosed() {

            }

            @Override
            public void onNativeFailedToLoad() {

            }

            @Override
            public void onBuildNativeAd(@NonNull NativeAd ad, boolean useCustomNativeLayout) {

            }
        });
    }
}