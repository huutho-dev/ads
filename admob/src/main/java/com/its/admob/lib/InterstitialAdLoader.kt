package com.its.admob.lib

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdLoader {
    const val intersTag = "IntersAd"

    private var isLoading = false
    private var lastTimeAdShow = 0L
    private var interstitial: InterstitialAd? = null
    private var loadFailureCount = 0


    internal fun loadInterstitial(
        onAdLoaded: () -> Unit = {},
        onAdFailedToLoad: () -> Unit = {},
        onAdWaitingTimeoutLoad: () -> Unit = {}
    ) {
        if (AdMobLib.isPremium)
            return

        if (isLoading)
            return

        if (System.currentTimeMillis() - lastTimeAdShow < AdMobLib.timeDelayToLoad) {
            onAdWaitingTimeoutLoad.invoke()
            return
        }

        isLoading = true
        interstitial = null

        InterstitialAd.load(
            AdMobLib.application,
            AdMobLib.interstitialAdUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    Log.i(intersTag, "onIntersLoaded ${ad.adUnitId}")
                    isLoading = false
                    interstitial = ad
                    loadFailureCount = 0
                    onAdLoaded.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.i(intersTag, "onIntersFailedToLoad ${loadAdError.message}")
                    isLoading = false
                    interstitial = null
                    onAdFailedToLoad.invoke()
                    loadFailureCount++
                    if (loadFailureCount < 10) {
                        runDelay(loadFailureCount * 10000L) {
                            loadInterstitial()
                        }
                    }
                }
            }
        )
    }

    fun showInterstitial(
        activity: FragmentActivity?,
        onShowInterstitial: (showCode: AdMobLib.AdShowCode) -> Unit = {}
    ) {
        activity ?: return

        if (AdMobLib.isPremium)
            return

        if (System.currentTimeMillis() - lastTimeAdShow < AdMobLib.timeDelayToLoad) {
            Log.i(intersTag, "onShowInterstitial WAITING")
            onShowInterstitial.invoke(AdMobLib.AdShowCode.WAITING)
            return
        }

        if (isLoading) {
            Log.i(intersTag, "onShowInterstitial LOADING")
            onShowInterstitial.invoke(AdMobLib.AdShowCode.LOADING)
            return
        }

        if (!isLoading && interstitial == null && loadFailureCount < 10) {
            Log.i(intersTag, "onShowInterstitial RELOAD")
            onShowInterstitial.invoke(AdMobLib.AdShowCode.RELOAD)
            loadInterstitial()
            return
        }

        if (!isLoading && interstitial != null) {
            interstitial?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.i(intersTag, "onIntersFailedToShowFullScreenContent ${adError.message}")
                    interstitial = null
                    isLoading = false
                    onShowInterstitial.invoke(AdMobLib.AdShowCode.FAILED)
                    loadInterstitial()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    Log.i(intersTag, "onIntersShowedFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.i(intersTag, "onIntersDismissedFullScreenContent")
                    interstitial = null
                    isLoading = false
                    lastTimeAdShow = System.currentTimeMillis()
                    onShowInterstitial.invoke(AdMobLib.AdShowCode.DISMISS)
                    runDelay(AdMobLib.timeDelayToLoad) {
                        loadInterstitial()
                    }
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(intersTag, "onIntersImpression")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(intersTag, "onIntersClicked")
                }
            }
            interstitial?.show(activity)
        } else {
            onShowInterstitial.invoke(AdMobLib.AdShowCode.FAILED)
        }
    }

    fun checkIntersLoaded() = !isLoading && interstitial != null

}