package com.sdk.ads.manager.nativeAd

import com.sdk.ads.template.google.nativetemplates.TemplateView
import com.sdk.ads.manager.AdManager

class NativeManager(adUnitIdMap : MutableMap<String, String>) : AdManager<NativeAd>(adUnitIdMap) {

    fun showNativeAd(
        key: String,
        adTemplateView: TemplateView,
        useCustomLayout: Boolean,
        nativeAdListener: NativeAdListener
    ) {
        findAdUnitId(key)?.let {
            NativeAd(adTemplateView, it, useCustomLayout, nativeAdListener).showNative()
        }
    }
}