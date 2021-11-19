package com.sdk.ads.manager.banner

import android.view.ViewGroup
import com.google.android.gms.ads.AdSize
import com.sdk.ads.manager.AdManager

class BannerManager(adUnitIdMap : MutableMap<String, String>) : AdManager<BannerAd>(adUnitIdMap) {

    fun showBanner(key: String, viewGroup: ViewGroup, adSize: AdSize) {
        BannerAd(
            viewGroup,
            findAdUnitId(key)!!,
            adSize,
        ).showBanner()
    }
}