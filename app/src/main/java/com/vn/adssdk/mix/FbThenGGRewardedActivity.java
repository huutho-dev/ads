package com.vn.adssdk.mix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;

import kotlin.Unit;

public class FbThenGGRewardedActivity extends AppCompatActivity {

    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_then_ggrewarded);

        btn =  findViewById(R.id.btnWatch);
        btn.setEnabled(false);

        AdManager.loadRewarded(this, () -> {
            btn.setEnabled(true);
            return Unit.INSTANCE;
        });

        findViewById(R.id.btnWatch).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xem video để nhận 10 triệu đô")
                .setMessage("Tin người vl")
                .setPositiveButton("Cút", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("OK", (dialog, which) -> {
                    AdManager.showRewarded(FbThenGGRewardedActivity.this);
                    dialog.dismiss();
                })
                .create();

            alertDialog.show();
        });

    }
}