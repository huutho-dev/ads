package com.vn.adssdk.google;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;

public class GoogleNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_native);
        AdManager.showGGNative(findViewById(R.id.templateView));
    }
}