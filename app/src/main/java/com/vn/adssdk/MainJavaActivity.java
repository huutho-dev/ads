package com.vn.adssdk;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sdk.ads.AdManager;
import com.vn.adssdk.databinding.ActivityMainJavaBinding;

public class MainJavaActivity extends AppCompatActivity {

    String adsJsonConfig = "{\n" +
            "  \"isEnable\" : true,\n" +
            "  \"ads\": {\n" +
            "    \"google\": {\n" +
            "      \"banner\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/6300978111\"\n" +
            "      },\n" +
            "      \"interstitial\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/1033173712\"\n" +
            "      },\n" +
            "      \"rewarded\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/5224354917\"\n" +
            "      },\n" +
            "      \"native\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"ca-app-pub-3940256099942544/2247696110\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"facebook\": {\n" +
            "      \"banner\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233085135554539\"\n" +
            "      },\n" +
            "      \"interstitial\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233084782221241\"\n" +
            "      },\n" +
            "      \"rewarded\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233085135554539\"\n" +
            "      },\n" +
            "      \"native\": {\n" +
            "        \"isEnable\": true,\n" +
            "        \"adUnitId\" : \"233072595555793_233084965554556\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainJavaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdManager.initialize(this);
        AdManager.setAdsConfig(adsJsonConfig, false);

        binding = ActivityMainJavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_java);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show());

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_java);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
    }
}