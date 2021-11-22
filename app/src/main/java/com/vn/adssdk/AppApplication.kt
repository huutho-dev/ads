package com.vn.adssdk

import androidx.multidex.MultiDexApplication

class AppApplication : MultiDexApplication() {


    var adsJsonConfig = """{
  "isEnableAds": true,
  "useFirstUnitId": false,
  "ads": [
    "GGBanner#GoogleBannerActivity#ca-app-pub-3940256099942544/6300978111#enable",
    "GGInters#GoogleInterstitialActivity#ca-app-pub-3940256099942544/1033173712#enable",
    "GGRewarded#GoogleRewardedActivity#ca-app-pub-3940256099942544/5224354917#enable",
    "GGNative#GoogleNativeActivity#ca-app-pub-3940256099942544/2247696110#enable",
    "GGOpenApp#MainFragment#ca-app-pub-3940256099942544/3419835294#enable",
    "FbBanner#MainFragment#233072595555793_233085135554539#enable",
    "FbInters#MainFragment#233072595555793_233084782221241#enable",
    "FbRewarded#MainFragment#233072595555793_233085135554539#enable",
    "FbNative#MainFragment#233072595555793_233084965554556#enable"
  ]
}"""


    override fun onCreate() {
        super.onCreate()
    }
}