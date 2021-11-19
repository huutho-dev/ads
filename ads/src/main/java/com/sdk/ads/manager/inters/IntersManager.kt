package com.sdk.ads.manager.inters

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sdk.ads.intersTag
import com.sdk.ads.manager.AdManager

class IntersManager(adUnitIdMap : MutableMap<String, String>) : AdManager<IntersAd>(adUnitIdMap) {

    fun loadInter(
        activity: AppCompatActivity,
        key: String,
        onAdLoaded: () -> Unit = {},
        onAdLoadFailure: (errorCode: String) -> Unit = {}
    ) {
        checkCanLoadAd(key,
            onCanLoad = {
                val newInterAd = IntersAd(activity, findAdUnitId(key)!!).apply {
                    loadInters(object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            super.onAdLoaded(ad)
                            onAdLoaded.invoke()
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            super.onAdFailedToLoad(error)
                            clearInterAdLoading(key)
                            onAdLoadFailure.invoke(error.message)
                        }
                    })
                }
                saveInterAdLoading(key, newInterAd)
            },
            onCanNotLoad = {
                Log.i(intersTag, it)
                onAdLoadFailure.invoke(LOAD_TIME_INVALID_ERROR)
            }
        )
    }

    fun showInterAd(
        key: String,
        onAdDismissed: () -> Unit = {},
        onAdFailedToShow: () -> Unit = {}
    ) {
        getAdLoadingByKey(key)?.showInters(object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                super.onAdFailedToShowFullScreenContent(error)
                clearInterAdLoading(key)
                onAdFailedToShow.invoke()
            }
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                saveAdShowTime(key)
                onAdDismissed.invoke()
            }
        })
    }


}