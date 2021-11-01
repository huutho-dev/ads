package com.sdk.ads.google

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sdk.ads.AdManager

class GoogleRewarded {

    private var mRewardedAd: RewardedAd? = null
    private var mRewardedAdLoading = false

    fun loadRewarded(
        activity: Activity,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdLoaded: (rewardedAd: RewardedAd) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError?) -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {}
    ) {
        Log.i(AdManager.TAG, "GoogleRewarded loadRewarded")
        if (AdManager.isEnableAd && AdManager.googleRewarded?.isEnable == true && !AdManager.googleRewarded?.adUnitId.isNullOrBlank()) {
            mRewardedAdLoading = true
            RewardedAd.load(activity, AdManager.googleRewarded?.adUnitId!!,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e(AdManager.TAG, "GoogleRewarded onAdFailedToLoad (${adError.message})")
                        mRewardedAd = null
                        mRewardedAdLoading = false
                        onAdFailedToLoad.invoke(adError)
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        Log.e(AdManager.TAG, "onAdLoaded $rewardedAd")
                        mRewardedAd = rewardedAd
                        mRewardedAdLoading = false

                        mRewardedAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdShowedFullScreenContent() {
                                    Log.e(AdManager.TAG, "onAdShowedFullScreenContent")
                                    onAdShowedFullScreenContent.invoke()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    Log.e(
                                        AdManager.TAG,
                                        "onAdFailedToShowFullScreenContent $adError"
                                    )
                                    mRewardedAd = null
                                    onAdFailedToShowFullScreenContent.invoke(adError)
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    Log.e(AdManager.TAG, "onAdDismissedFullScreenContent")
                                    mRewardedAd = null
                                    onAdDismissedFullScreenContent.invoke()
                                }
                            }

                        onAdLoaded.invoke(rewardedAd)
                    }
                })
        } else {
            Log.i(AdManager.TAG, "GoogleRewarded enable = false or adUnitNull")
        }
    }

    fun showRewardedAdAfterUserConfirm(
        activity: Activity,
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit = {}
    ) {
        fun showAd() {
            Log.i(AdManager.TAG, "GoogleRewarded showAd")
            mRewardedAd?.show(activity) {
                val rewardAmount = it.amount
                val rewardType = it.type
                Log.e(AdManager.TAG, "onUserEarnedReward $rewardAmount $rewardType ")
                onUserEarnedReward.invoke(it)
            }
        }

        if (mRewardedAd != null) {
            showAd()
        } else {
            Log.i(AdManager.TAG, "The rewarded ad wasn't ready yet.")
        }
    }
}