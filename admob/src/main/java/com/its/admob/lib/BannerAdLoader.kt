package com.its.admob.lib

import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.*

object BannerAdLoader {
    private const val bannerTag = "BannerAd"

    @JvmStatic
    @JvmOverloads
    fun addBanner(
        viewGroup: ViewGroup,
        adUnitId: String,
        adSize: AdSize = AdSize.FULL_BANNER,
        onCheckedIsPremium : () -> Unit = {},
        onAdClosed: () -> Unit = {},
        onAdFailedToLoad: () -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        if (AdMobLib.isPremium){
            onCheckedIsPremium.invoke()
            return
        }

        val adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                Log.i(bannerTag, "onBannerClosed")
                onAdClosed.invoke()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.i(bannerTag, "onBannerFailedToLoad ${loadAdError.message}")
                onAdFailedToLoad.invoke()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.i(bannerTag, "onBannerOpened")
                onAdOpened.invoke()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.i(bannerTag, "onBannerLoaded")
                onAdLoaded.invoke()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.i(bannerTag, "onBannerClicked")
                onAdClicked.invoke()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.i(bannerTag, "onBannerImpression")
                onAdImpression.invoke()
            }
        }

        val adView = AdView(viewGroup.context).apply {
            this.adUnitId = adUnitId
            this.adSize = adSize
            this.adListener = adListener
            loadAd(AdRequest.Builder().build())
        }

        viewGroup.removeAllViews()
        viewGroup.addView(adView)
    }
}