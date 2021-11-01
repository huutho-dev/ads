package com.sdk.ads.facebook

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.RewardedVideoAdListener
import com.sdk.ads.AdManager


class FacebookRewarded {
    private var rewardedVideoAd: RewardedVideoAd? = null

    fun loadRewarded(
        activity: Activity,
        onError: (error: AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onRewardedVideoCompleted: () -> Unit = {},
        onRewardedVideoClosed: () -> Unit = {},
    ) {
        Log.i(AdManager.TAG, "FacebookRewarded loadRewarded")
        if (AdManager.isEnableAd
            && AdManager.facebookRewarded?.isEnable == true
            && !AdManager.facebookRewarded?.adUnitId.isNullOrBlank()
        ) {
            rewardedVideoAd = RewardedVideoAd(activity, AdManager.facebookRewarded?.adUnitId)
            rewardedVideoAd!!.loadAd(
                rewardedVideoAd!!.buildLoadAdConfig()
                    .withAdListener(object : RewardedVideoAdListener {
                        override fun onError(ad: Ad?, error: AdError?) {
                            Log.i(
                                AdManager.TAG,
                                "FacebookRewarded onError = ${error?.errorMessage}"
                            )
                            onError.invoke(error)
                            rewardedVideoAd = null
                        }

                        override fun onAdLoaded(ad: Ad?) {
                            Log.i(AdManager.TAG, "FacebookRewarded onAdLoaded = ${ad?.placementId}")
                            onAdLoaded.invoke()
                        }

                        override fun onAdClicked(ad: Ad?) {
                            Log.i(
                                AdManager.TAG,
                                "FacebookRewarded onAdClicked = ${ad?.placementId}"
                            )
                            onAdClicked.invoke()
                        }

                        override fun onLoggingImpression(ad: Ad?) {
                            Log.i(
                                AdManager.TAG,
                                "FacebookRewarded onLoggingImpression = ${ad?.placementId}"
                            )
                            onLoggingImpression.invoke()
                        }

                        override fun onRewardedVideoCompleted() {
                            Log.i(AdManager.TAG, "FacebookRewarded onRewardedVideoCompleted")
                            onRewardedVideoCompleted.invoke()
                        }

                        override fun onRewardedVideoClosed() {
                            Log.i(AdManager.TAG, "FacebookRewarded onRewardedVideoClosed")
                            onRewardedVideoClosed.invoke()
                            rewardedVideoAd = null
                        }
                    })
                    .build()
            )
        } else {
            Log.i(AdManager.TAG, "FacebookRewarded enable = false or adUnitNull")
        }
    }


    fun showRewarded() {
        Log.i(AdManager.TAG, "FacebookRewarded showRewarded")
        rewardedVideoAd?.show()
    }
}