package com.vn.adssdk.google;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;

import kotlin.Unit;

public class GoogleRewardedActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_rewarded);

        btn = findViewById(R.id.btnWatch);

        loadRewarded();

        findViewById(R.id.btnWatch).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xem video để nhận 10 triệu đô")
                .setMessage("Tin người vl")
                .setPositiveButton("Cút", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("OK", (dialog, which) -> {
                        AdManager.showGGRewardedAfterAskUser(this, rewardItem -> {
                            Toast.makeText(this, "Nhận thưởng thành công", Toast.LENGTH_SHORT).show();
                        });
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

    private void loadRewarded(){
        btn.setEnabled(false);
        AdManager.loadGGRewarded(this,
            () -> {
                btn.setEnabled(true);
                return Unit.INSTANCE;
            },
            loadAdError -> {
                btn.setText(loadAdError.getMessage());
                btn.setEnabled(false);
                return Unit.INSTANCE;
            }
        );
    }
}