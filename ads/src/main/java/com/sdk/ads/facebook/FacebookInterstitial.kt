package com.sdk.ads.facebook

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.sdk.ads.AdManager

class FacebookInterstitial {

    private var interstitialAd: InterstitialAd? = null

    fun loadInterstitial(
        activity: Activity,
        onError: (adError: AdError?) -> Unit,
        onAdLoaded: () -> Unit,
        onAdClicked: () -> Unit,
        onLoggingImpression: () -> Unit,
        onInterstitialDisplayed: () -> Unit,
        onInterstitialDismissed: () -> Unit,
    ) {
        Log.i(AdManager.TAG, "FacebookInterstitial loadInterstitial")
        if (AdManager.isEnableAd
            && AdManager.facebookInterstitial?.isEnable == true
            && !AdManager.facebookInterstitial?.adUnitId.isNullOrBlank()
        ) {
            interstitialAd = InterstitialAd(activity, AdManager.facebookInterstitial?.adUnitId)

            val adConfig = interstitialAd!!.buildLoadAdConfig()
                .withAdListener(object : InterstitialAdListener {
                    override fun onError(ad: Ad?, adError: AdError?) {
                        Log.i(
                            AdManager.TAG,
                            "FacebookInterstitial onError = ${adError?.errorMessage}"
                        )
                        onError.invoke(adError)
                        interstitialAd = null
                    }

                    override fun onAdLoaded(ad: Ad?) {
                        Log.i(AdManager.TAG, "FacebookInterstitial onAdLoaded = ${ad?.placementId}")
                        onAdLoaded.invoke()
                    }

                    override fun onAdClicked(ad: Ad?) {
                        Log.i(
                            AdManager.TAG,
                            "FacebookInterstitial onAdClicked = ${ad?.placementId}"
                        )
                        onAdClicked.invoke()
                    }

                    override fun onLoggingImpression(ad: Ad?) {
                        Log.i(
                            AdManager.TAG,
                            "FacebookInterstitial onLoggingImpression = ${ad?.placementId}"
                        )
                        onLoggingImpression.invoke()
                    }

                    override fun onInterstitialDisplayed(ad: Ad?) {
                        Log.i(
                            AdManager.TAG,
                            "FacebookInterstitial onInterstitialDisplayed = ${ad?.placementId}"
                        )
                        onInterstitialDisplayed.invoke()
                    }

                    override fun onInterstitialDismissed(ad: Ad?) {
                        Log.i(
                            AdManager.TAG,
                            "FacebookInterstitial onInterstitialDismissed = ${ad?.placementId}"
                        )
                        onInterstitialDismissed.invoke()
                        interstitialAd = null
                    }
                })
                .build()
            interstitialAd!!.loadAd(adConfig)
        } else {
            Log.i(AdManager.TAG, "FacebookInterstitial enable = false or adUnit = Null")
        }
    }


    fun showInterstitial() {
        Log.i(AdManager.TAG, "FacebookInterstitial showInterstitial")
        interstitialAd?.show()
    }
}