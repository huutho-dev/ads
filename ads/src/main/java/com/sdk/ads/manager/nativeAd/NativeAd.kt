package com.sdk.ads.manager.nativeAd

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.sdk.ads.template.google.nativetemplates.NativeTemplateStyle
import com.sdk.ads.template.google.nativetemplates.TemplateView
import com.sdk.ads.manager.AdLoading
import com.sdk.ads.nativeTag

class NativeAd(
    private val _adNativeView: TemplateView,
    _adUnitId: String,
    private val _useCustomNativeLayout: Boolean = false,
    private val _nativeAdListener: NativeAdListener = object : NativeAdListener {}
) : AdListener(), AdLoading {

    private val adLoader: AdLoader = AdLoader.Builder(_adNativeView.context, _adUnitId)
        .forNativeAd { ad: NativeAd -> buildNativeAd(ad) }
        .withAdListener(this)
        .build()

    fun showNative() {
        _adNativeView.visibility = View.GONE
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun buildNativeAd(ad: NativeAd) {
        if (ViewTreeLifecycleOwner.get(_adNativeView)?.lifecycle?.currentState == Lifecycle.State.DESTROYED) {
            Log.i(nativeTag, "onBuildNativeAd() # Lifecycle.State.DESTROYED ad")
            ad.destroy()
            return
        }
        _adNativeView.visibility = View.VISIBLE
        if (_useCustomNativeLayout) {
            Log.i(nativeTag, "onBuildNativeAd() # use CUSTOM native layout")
            _nativeAdListener.onBuildNativeAd(ad, true)
        } else {
            val styles = NativeTemplateStyle.Builder()
                .withMainBackgroundColor(ColorDrawable(Color.TRANSPARENT))
                .build()

            _adNativeView.setStyles(styles)
            _adNativeView.setNativeAd(ad)

            Log.i(nativeTag, "onBuildNativeAd() # use DEFAULT native layout")
            _nativeAdListener.onBuildNativeAd(ad, false)
        }
    }


    override fun onAdClosed() {
        super.onAdClosed()
        Log.i(nativeTag, "onNativeClosed()")
        _nativeAdListener.onNativeClosed()
    }

    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        super.onAdFailedToLoad(loadAdError)
        Log.i(nativeTag, "onNativeFailedToLoad() ${loadAdError.message}")
        _nativeAdListener.onNativeFailedToLoad()
    }

    override fun onAdOpened() {
        super.onAdOpened()
        Log.i(nativeTag, "onNativeOpened()")
        _nativeAdListener.onNativeOpened()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        Log.i(nativeTag, "onNativeLoaded()")
        _nativeAdListener.onNativeLoaded()
    }

    override fun onAdClicked() {
        super.onAdClicked()
        Log.i(nativeTag, "onNativeClicked()")
        _nativeAdListener.onNativeClicked()
    }

    override fun onAdImpression() {
        super.onAdImpression()
        Log.i(nativeTag, "onNativeImpression()")
        _nativeAdListener.onNativeImpression()
    }
}