package com.vn.adssdk.google;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;

import kotlin.Unit;

public class GoogleRewardedActivity extends AppCompatActivity {

    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_rewarded);

        btn =  findViewById(R.id.btnWatch);
        btn.setEnabled(false);

        AdManager.loadGGRewarded(this, loadAdError -> {
           btn.setText(loadAdError.getMessage());
           btn.setEnabled(false);
            return Unit.INSTANCE;
        }, rewardedAd -> {
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
                        AdManager.showGGRewarded(this, rewardItem -> {
                            Toast.makeText(this, "Nhận thưởng thành công", Toast.LENGTH_SHORT).show();
                            return Unit.INSTANCE;
                        });
                        dialog.dismiss();
                    })
                    .create();

            alertDialog.show();
        });
    }
}