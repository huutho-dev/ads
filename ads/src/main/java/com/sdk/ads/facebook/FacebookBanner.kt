package com.sdk.ads.facebook

import android.util.Log
import android.view.ViewGroup
import com.facebook.ads.*
import com.sdk.ads.AdManager


class FacebookBanner {

    fun showBanner(
        viewGroup: ViewGroup,
        adSize: AdSize = AdSize.BANNER_HEIGHT_50,
        onError: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {}
    ) {
        Log.i(AdManager.TAG, "FacebookBanner showBanner")
        if (!AdManager.isEnableAd
            || AdManager.facebookBanner == null
            || AdManager.facebookBanner?.isEnable != true
            || AdManager.facebookBanner?.adUnitId.isNullOrBlank()
        ) {
            Log.i(AdManager.TAG, "FacebookBanner enable = false or adUnitNull")
            return
        }


        val adView = AdView(viewGroup.context, "${AdManager.facebookBanner!!.adUnitId}", adSize)

        val listener = object : AdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                Log.i(AdManager.TAG, "FacebookBanner onError = ${adError?.errorMessage}")
                onError.invoke()
            }

            override fun onAdLoaded(ad: Ad?) {
                Log.i(AdManager.TAG, "FacebookBanner onAdLoaded = ${ad?.placementId}")
                onAdLoaded.invoke()
            }

            override fun onAdClicked(ad: Ad?) {
                Log.i(AdManager.TAG, "FacebookBanner onAdClicked = ${ad?.placementId}")
                onAdClicked.invoke()
            }

            override fun onLoggingImpression(ad: Ad?) {
                Log.i(AdManager.TAG, "FacebookBanner onLoggingImpression = ${ad?.placementId}")
                onLoggingImpression.invoke()
            }

        }

        val adConfig = adView.buildLoadAdConfig()
            .withAdListener(listener)
            .build()

        viewGroup.removeAllViews()
        viewGroup.addView(adView)
        adView.loadAd(adConfig)
    }
}