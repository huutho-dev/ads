package com.sdk.ads.manager.inters

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sdk.ads.intersTag
import com.sdk.ads.manager.AdLoading

class IntersAd(
    private val _activity: Activity,
    private val _adUnitId: String,
) : AdLoading {
    private var _interstitial: InterstitialAd? = null
    private var _isLoading = false


    fun loadInters(callback: InterstitialAdLoadCallback) {
        if (!_isLoading) {
            _isLoading = true
            InterstitialAd.load(
                _activity,
                _adUnitId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)
                        Log.i(intersTag, "onIntersLoaded ${interstitialAd.adUnitId}")
                        _isLoading = false
                        _interstitial = interstitialAd
                        callback.onAdLoaded(interstitialAd)
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        _isLoading = false
                        _interstitial = null
                        Log.i(intersTag, "onIntersFailedToLoad ${loadAdError.message}")
                        callback.onAdFailedToLoad(loadAdError)
                    }
                })
        }
    }

    fun showInters(callback: FullScreenContentCallback) {
        if (!_isLoading && _interstitial != null) {
            _interstitial?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.i(intersTag, "onIntersFailedToShowFullScreenContent ${adError.message}")
                    _interstitial = null
                    callback.onAdFailedToShowFullScreenContent(adError)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    Log.i(intersTag, "onIntersShowedFullScreenContent")
                    callback.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.i(intersTag, "onIntersDismissedFullScreenContent")
                    _interstitial = null
                    callback.onAdDismissedFullScreenContent()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(intersTag, "onIntersImpression")
                    callback.onAdImpression()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(intersTag, "onIntersClicked")
                    callback.onAdClicked()
                }
            }
            _interstitial?.show(_activity)
        }
    }

    override fun isLoading(): Boolean {
        return _isLoading
    }
}