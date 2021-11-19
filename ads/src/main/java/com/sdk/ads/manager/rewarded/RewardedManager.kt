package com.sdk.ads.manager.rewarded

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sdk.ads.manager.AdManager
import com.sdk.ads.rewardedTag


class RewardedManager(adUnitIdMap : MutableMap<String, String>) : AdManager<RewardAd>(adUnitIdMap) {

    fun loadReward(
        activity: AppCompatActivity,
        key: String,
        onAdLoaded: () -> Unit = {},
        onAdLoadFailure: (errorCode: String) -> Unit = {}
    ) {
        checkCanLoadAd(key,
            onCanLoad = {
                val newInterAd = RewardAd(activity, findAdUnitId(key)!!).apply {
                    loadRewarded(object : RewardedAdLoadCallback() {

                        override fun onAdLoaded(ad: RewardedAd) {
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
                Log.i(rewardedTag, it)
                onAdLoadFailure.invoke(LOAD_TIME_INVALID_ERROR)
            }
        )
    }

    fun showReward(
        key: String,
        onAdDismissed: () -> Unit = {},
        onAdFailedToShow: () -> Unit = {}
    ) {
        getAdLoadingByKey(key)?.showRewarded(object : FullScreenContentCallback() {
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