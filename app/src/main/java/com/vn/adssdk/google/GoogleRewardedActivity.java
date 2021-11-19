package com.vn.adssdk.google;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.ads.AdSDK;
import com.vn.adssdk.R;

import kotlin.Unit;

public class GoogleRewardedActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_rewarded);

        btn = findViewById(R.id.btnWatch);

        findViewById(R.id.btnWatch).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xem video để nhận 10 triệu đô")
                .setMessage("Tin người vl")
                .setPositiveButton("Cút", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("OK", (dialog, which) -> {
                        AdSDK.showRewarded("rewarded1");
                    dialog.dismiss();
                })
                .create();

            alertDialog.show();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(this::loadRewarded, 1000);
    }

    private void loadRewarded() {
        btn.setEnabled(false);
        AdSDK.loadRewarded(this, "rewarded1", () -> {
            btn.setText("Click to show");
            btn.setEnabled(true);
            return Unit.INSTANCE;
        }, s -> {
            btn.setText(s);
            btn.setEnabled(false);
            return Unit.INSTANCE;
        });
    }
}