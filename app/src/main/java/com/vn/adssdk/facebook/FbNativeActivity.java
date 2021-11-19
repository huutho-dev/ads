package com.vn.adssdk.facebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdk.ads.AdSDK;
import com.vn.adssdk.R;

public class FbNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_native);

        /*AdManager.showFbNative(findViewById(R.id.nativeAdLayout));*/
    }
}