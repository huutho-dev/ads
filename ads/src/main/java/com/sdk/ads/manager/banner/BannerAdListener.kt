package com.sdk.ads.manager.banner

interface BannerAdListener {
    fun onBannerLoaded() {}
    fun onBannerClicked() {}
    fun onBannerFailedToLoad() {}
    fun onBannerClosed() {}
    fun onBannerOpened() {}
    fun onBannerImpression() {}
}