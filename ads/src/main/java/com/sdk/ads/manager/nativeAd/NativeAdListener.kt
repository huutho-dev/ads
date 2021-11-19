package com.sdk.ads.manager.nativeAd

import com.google.android.gms.ads.nativead.NativeAd

interface NativeAdListener {
    fun onBuildNativeAd(ad: NativeAd, useCustomNativeLayout : Boolean) {}
    fun onNativeFailedToLoad() {}
    fun onNativeClosed() {}
    fun onNativeOpened() {}
    fun onNativeLoaded() {}
    fun onNativeClicked() {}
    fun onNativeImpression() {}
}