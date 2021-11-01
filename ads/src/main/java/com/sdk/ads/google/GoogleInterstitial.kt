package com.sdk.ads.google

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sdk.ads.AdManager

class GoogleInterstitial {

    private var mInterstitialAd: InterstitialAd? = null
    private var mInterstitialAdLoading = false

    fun loadInterstitial(
        activity: Activity,
        onAdLoaded: (interstitialAd: InterstitialAd) -> Unit = {},
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {},
        onAdImpression: () -> Unit = {},
        onAdClicked: () -> Unit = {},
    ) {
        Log.i(AdManager.TAG, "GoogleBanner loadInterstitial")
        if (AdManager.isEnableAd
            && AdManager.googleInterstitial?.isEnable == true
            && !AdManager.googleInterstitial?.adUnitId.isNullOrBlank()
        ) {
            InterstitialAd.load(
                activity,
                AdManager.googleInterstitial?.adUnitId!!,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)
                        Log.e(
                            AdManager.TAG,
                            "GoogleInterstitial onAdLoaded - ${interstitialAd.adUnitId}"
                        )
                        mInterstitialAd = interstitialAd
                        interstitialAd.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    mInterstitialAd = null
                                    Log.e(
                                        AdManager.TAG,
                                        "GoogleInterstitial onAdFailedToShowFullScreenContent - $adError"
                                    )
                                    onAdFailedToShowFullScreenContent.invoke(adError)
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    Log.e(
                                        AdManager.TAG,
                                        "GoogleInterstitial onAdShowedFullScreenContent"
                                    )
                                    onAdShowedFullScreenContent.invoke()
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    mInterstitialAd = null
                                    Log.e(
                                        AdManager.TAG,
                                        "GoogleInterstitial onAdDismissedFullScreenContent"
                                    )
                                    onAdDismissedFullScreenContent.invoke()
                                }

                                override fun onAdImpression() {
                                    super.onAdImpression()
                                    Log.e(AdManager.TAG, "GoogleInterstitial onAdImpression")
                                    onAdImpression.invoke()
                                }

                                override fun onAdClicked() {
                                    super.onAdClicked()
                                    Log.e(AdManager.TAG, "GoogleInterstitial onAdClicked")
                                    onAdClicked.invoke()
                                }
                            }


                        mInterstitialAdLoading = false
                        onAdLoaded.invoke(interstitialAd)
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        Log.e(
                            AdManager.TAG,
                            "GoogleInterstitial onAdFailedToLoad - $adError"
                        )
                        mInterstitialAd = null
                        mInterstitialAdLoading = false
                        onAdFailedToLoad.invoke(adError)
                    }
                })
        } else {
            Log.i(AdManager.TAG, "GoogleInterstitial enable = false or adUnitNull")
        }
    }


    fun showInterstitialGoogle(activity: Activity) {
        Log.i(AdManager.TAG, "GoogleInterstitial showInterstitialGoogle")
        mInterstitialAd?.show(activity)
    }
}