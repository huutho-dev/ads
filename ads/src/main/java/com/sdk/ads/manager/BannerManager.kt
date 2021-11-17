package com.sdk.ads.manager

import android.util.Log
import android.view.ViewGroup
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.google.android.gms.ads.*
import com.sdk.ads.AdManager
import com.sdk.ads.Constants

class BannerManager(private val adUnitManager: AdUnitManager) {

    fun showGGBanner(
        viewGroup: ViewGroup,
        screenLabel: String,
        adSize: AdSize = AdSize.FULL_BANNER,
        onAdClosed: () -> Unit = {},
        onAdFailedToLoad: (error: LoadAdError) -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        val adUnit = adUnitManager.findAdUnit(Constants.AdType.GGBanner, screenLabel)
        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()){
            val adView = AdView(viewGroup.context)
            adView.adUnitId = adUnit.adUnitId
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
            viewGroup.removeAllViews()
            adView.loadAd(AdRequest.Builder().build())
            viewGroup.addView(adView)
        }
    }

    fun showFacebookBanner(
        viewGroup: ViewGroup,
        screenLabel: String,
        adSize: com.facebook.ads.AdSize = com.facebook.ads.AdSize.BANNER_HEIGHT_50,
        onError: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {}
    ) {

        val adUnit = adUnitManager.findAdUnit(Constants.AdType.FbBanner, screenLabel)
        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()){
            val adView = com.facebook.ads.AdView(viewGroup.context, adUnit.adUnitId, adSize)
            val listener = object : com.facebook.ads.AdListener {
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

            adView.loadAd(adConfig)
            viewGroup.removeAllViews()
            viewGroup.addView(adView)
        }
    }

    fun showFbThenGGBanner() {

    }

    fun showGGThenFbBanner() {}
}