package com.sdk.ads.manager.banner

import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.sdk.ads.bannerTag
import com.sdk.ads.manager.AdLoading

class BannerAd(
    private val _viewGroup: ViewGroup,
    private val _adUnitId: String,
    private val _adSize: AdSize = AdSize.FULL_BANNER,
    private val _bannerAdListener: BannerAdListener = object : BannerAdListener {}
) : AdListener(), AdLoading {

    fun showBanner() {
        _viewGroup.addView(AdView(_viewGroup.context).apply {
            adUnitId = _adUnitId
            adSize = _adSize
            adListener = this@BannerAd
            loadAd(AdRequest.Builder().build())
        })
    }

    override fun onAdClosed() {
        super.onAdClosed()
        Log.i(bannerTag, "onBannerClosed")
        _bannerAdListener.onBannerClosed()
    }

    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        super.onAdFailedToLoad(loadAdError)
        Log.i(bannerTag, "onBannerFailedToLoad ${loadAdError.message}")
        _bannerAdListener.onBannerFailedToLoad()
    }

    override fun onAdOpened() {
        super.onAdOpened()
        Log.i(bannerTag, "onBannerOpened")
        _bannerAdListener.onBannerOpened()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        Log.i(bannerTag, "onBannerLoaded")
        _bannerAdListener.onBannerLoaded()
    }

    override fun onAdClicked() {
        super.onAdClicked()
        Log.i(bannerTag, "onBannerClicked")
        _bannerAdListener.onBannerClicked()
    }

    override fun onAdImpression() {
        super.onAdImpression()
        Log.i(bannerTag, "onBannerImpression")
        _bannerAdListener.onBannerImpression()
    }
}