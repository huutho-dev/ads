package com.sdk.ads.google

import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.sdk.ads.AdManager

class GoogleBanner {

    fun showBannerGoogle(
        viewGroup: ViewGroup,
        adSize: AdSize = AdSize.FULL_BANNER,
        onAdClosed: () -> Unit = {},
        onAdFailedToLoad: (error: LoadAdError) -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        Log.i(AdManager.TAG, "GoogleBanner => showBannerGoogle")
        if (AdManager.isEnableAd && AdManager.googleBanner?.isEnable == true) {
            if (AdManager.googleBanner?.adUnitId.isNullOrBlank()) {
                Log.i(AdManager.TAG, "GoogleBanner enable = false or adUnitNull")
                return
            }
            val adView = AdView(viewGroup.context)
            adView.adUnitId = AdManager.googleBanner!!.adUnitId!!
            adView.adSize = adSize
            adView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.i(AdManager.TAG, "GoogleBanner onAdClosed()")
                    onAdClosed.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.i(AdManager.TAG, "GoogleBanner onAdFailedToLoad(${error.message})")
                    onAdFailedToLoad.invoke(error)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.i(AdManager.TAG, "GoogleBanner onAdOpened()")
                    onAdOpened.invoke()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i(AdManager.TAG, "GoogleBanner onAdLoaded()")
                    onAdLoaded.invoke()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(AdManager.TAG, "GoogleBanner onAdClicked()")
                    onAdClicked.invoke()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(AdManager.TAG, "GoogleBanner onAdImpression()")
                    onAdImpression.invoke()
                }
            }
            adView.loadAd(AdRequest.Builder().build())
            viewGroup.addView(adView)
        } else {
            Log.i(AdManager.TAG, "GoogleBanner enable = false or adUnitNull")
        }
    }
}