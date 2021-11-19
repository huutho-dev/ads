package com.sdk.ads.manager.rewarded

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sdk.ads.manager.AdLoading
import com.sdk.ads.rewardedTag

class RewardAd(
    private val _activity: Activity,
    private val _adUnitId: String,
) : AdLoading {
    private var _rewarded: RewardedAd? = null
    private var _isLoading = false

    private val onUserEarnedRewardListener = OnUserEarnedRewardListener {
        Log.i(rewardedTag, "OnUserEarnedRewardListener")
    }

    fun loadRewarded(callback: RewardedAdLoadCallback) {
        if (!_isLoading) {
            _isLoading = true
            RewardedAd.load(
                _activity,
                _adUnitId,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        super.onAdLoaded(rewardedAd)
                        _rewarded = rewardedAd
                        _isLoading = false
                        Log.i(rewardedTag, "onRewardedLoaded ${rewardedAd.adUnitId}")
                        callback.onAdLoaded(rewardedAd)
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        _rewarded = null
                        _isLoading = false
                        Log.i(rewardedTag, "onRewardedFailedToLoad ${loadAdError.message}")
                        callback.onAdFailedToLoad(loadAdError)
                    }
                })
        }
    }

    fun showRewarded(callback: FullScreenContentCallback) {
        if (!_isLoading && _rewarded != null) {
            _rewarded?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.i(rewardedTag, "onRewardedFailedToShowFullScreenContent ${adError.message}")
                    _rewarded = null
                    callback.onAdFailedToShowFullScreenContent(adError)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    Log.i(rewardedTag, "onRewardedShowedFullScreenContent")
                    callback.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.i(rewardedTag, "onRewardedDismissedFullScreenContent")
                    _rewarded = null
                    callback.onAdDismissedFullScreenContent()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(rewardedTag, "onRewardedImpression")
                    callback.onAdImpression()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(rewardedTag, "onRewardedClicked")
                    callback.onAdClicked()
                }
            }
            _rewarded?.show(_activity, onUserEarnedRewardListener)
        }
    }

    override fun isLoading(): Boolean {
        return _isLoading
    }

}