package com.vn.adssdk.facebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.sdk.ads.AdManager;
import com.vn.adssdk.R;

import kotlin.Unit;

public class FbRewardedActivity extends AppCompatActivity {

    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_rewarded);

        btn =  findViewById(R.id.btnWatch);
        btn.setEnabled(false);

        AdManager.loadFbRewarded(this, loadAdError -> {
            btn.setText(loadAdError.getErrorMessage());
            btn.setEnabled(false);
            return Unit.INSTANCE;
        },() -> {
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
                        AdManager.showFbReward();
                        dialog.dismiss();
                    })
                    .create();

            alertDialog.show();
        });
    }
}