package com.its.admob.lib

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object RewardedUtils {
    const val rewardedTag = "RewardedAd"

    private var rewarded: RewardedAd? = null
    private var isLoading = false

    private var loadFailureCount = 0

    fun checkPremiumBeforeLoadRewarded(onRewardLoaded: (isPremium : Boolean) -> Unit = {},) {
        if (AdMobLib.isPremium){
            onRewardLoaded.invoke(true)
            return
        }


        if (isLoading)
            return

        isLoading = true
        rewarded = null

        RewardedAd.load(
            AdMobLib.application,
            AdMobLib.rewardedAdUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Log.i(rewardedTag, "onRewardedLoaded ${rewardedAd.adUnitId}")
                    isLoading = false
                    rewarded = rewardedAd
                    loadFailureCount = 0
                    onRewardLoaded.invoke(false)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.i(rewardedTag, "onRewardedFailedToLoad ${loadAdError.message}")
                    isLoading = false
                    rewarded = null
                    loadFailureCount++

                    if (loadFailureCount <= 3) {
                        checkPremiumBeforeLoadRewarded(onRewardLoaded)
                    }
                }
            })
    }

    fun checkAdLoaded(): Boolean {
        return !isLoading && rewarded != null
    }

    fun showRewarded(
        activity: FragmentActivity,
        onUserEarnedReward: (item: RewardItem) -> Unit = {},
        onShowRewarded: (showCode: AdMobLib.AdShowCode) -> Unit = {},
    ) {
        if (AdMobLib.isPremium){
            onShowRewarded.invoke(AdMobLib.AdShowCode.PREMIUM)
            return
        }

        if (isLoading) {
            onShowRewarded.invoke(AdMobLib.AdShowCode.LOADING)
            return
        }

        if (loadFailureCount >= 3) {
            onShowRewarded.invoke(AdMobLib.AdShowCode.FAILED)
            return
        }

        if (!isLoading && rewarded != null) {
            rewarded?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.i(rewardedTag, "onRewardedFailedToShowFullScreenContent ${adError.message}")
                    onShowRewarded.invoke(AdMobLib.AdShowCode.FAILED)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    Log.i(rewardedTag, "onRewardedShowedFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.i(rewardedTag, "onRewardedDismissedFullScreenContent")
                    onShowRewarded.invoke(AdMobLib.AdShowCode.DISMISS)
                    isLoading = true
                    rewarded = null

                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(rewardedTag, "onRewardedImpression")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(rewardedTag, "onRewardedClicked")
                }
            }
            rewarded?.show(activity) {
                Log.i(rewardedTag, "onUserEarnedReward: $it")
                onUserEarnedReward.invoke(it)
            }
        }
    }
}