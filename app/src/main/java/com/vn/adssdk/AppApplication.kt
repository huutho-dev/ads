package com.vn.adssdk

import androidx.multidex.MultiDexApplication
import com.sdk.ads.AdSDK

class AppApplication : MultiDexApplication() {

    private var adsJsonConfig = """{
        "isEnable": true,
         "timeDelayToLoad": 10000,
        "ads": {
          "banner": [
            {
              "key": "banner1",
              "value": "ca-app-pub-3940256099942544/6300978111"
            },
            {
              "key": "banner2",
              "value": "ca-app-pub-3940256099942544/6300978111"
            }
          ],
          "interstitial": [
            {
              "key": "interstitial1",
              "value": "ca-app-pub-3940256099942544/1033173712"
            },
            {
              "key": "interstitial2",
              "value": "ca-app-pub-3940256099942544/1033173712"
            }
          ],
          "native": [
            {
              "key": "native1",
              "value": "ca-app-pub-3940256099942544/2247696110"
            },
            {
              "key": "native2",
              "value": "ca-app-pub-3940256099942544/2247696110"
            }
          ],
          "rewarded": [
            {
              "key": "rewarded1",
              "value": "ca-app-pub-3940256099942544/5224354917"
            },
            {
              "key": "rewarded2",
              "value": "ca-app-pub-3940256099942544/5224354917"
            }
          ],
          "open": [
            {
              "key": "openApp",
              "value": "ca-app-pub-3940256099942544/3419835294"
            }
          ]
        }
}"""


    override fun onCreate() {
        super.onCreate()
        AdSDK.init(this)
        AdSDK.setAdConfig(adsJsonConfig, false)
    }
}