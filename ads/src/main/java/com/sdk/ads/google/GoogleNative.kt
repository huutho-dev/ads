package com.sdk.ads.google

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.sdk.ads.AdManager
import com.sdk.ads.google.nativetemplates.NativeTemplateStyle
import com.sdk.ads.google.nativetemplates.TemplateView
import java.util.*

class GoogleNative {

    fun showGoogleNative(
        viewGroup: TemplateView,
        nativeAdOptions: NativeAdOptions? = null,
        onNativeAdLoaded: (ad: NativeAd) -> Boolean = { false },
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdClosed: () -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        Log.i(AdManager.TAG, "GoogleNative => showGoogleNative")
        if (AdManager.isEnableAd && AdManager.googleNative?.isEnable == true) {
            if (AdManager.googleNative?.adUnitId.isNullOrBlank()) {
                Log.i(AdManager.TAG, "GoogleNative enable = false or adUnitNull")
                return
            }
            viewGroup.visibility = View.GONE

            var adLoader: AdLoader? = null
            adLoader = AdLoader.Builder(viewGroup.context, AdManager.googleNative?.adUnitId!!)
                .forNativeAd { ad: NativeAd ->
                    Log.i(AdManager.TAG, "GoogleNative forNativeAd")
                    if (onNativeAdLoaded.invoke(ad)) {
                        return@forNativeAd
                    }

                    if (ViewTreeLifecycleOwner.get(viewGroup)?.lifecycle?.currentState == Lifecycle.State.DESTROYED) {
                        ad.destroy()
                        return@forNativeAd
                    }

                    if (adLoader?.isLoading == false) {
                        val styles = NativeTemplateStyle.Builder()
                            .withMainBackgroundColor(ColorDrawable(Color.TRANSPARENT))
                            .build()
                        viewGroup.setStyles(styles)
                        viewGroup.setNativeAd(ad)
                        viewGroup.visibility = View.VISIBLE
                    }
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i(AdManager.TAG, "GoogleNative onAdFailedToLoad(${adError.message})")
                        onAdFailedToLoad.invoke(adError)
                        viewGroup.visibility = View.GONE
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.i(AdManager.TAG, "GoogleNative onAdClosed()")
                        onAdClosed.invoke()
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        Log.i(AdManager.TAG, "GoogleNative onAdOpened()")
                        onAdOpened.invoke()
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.i(AdManager.TAG, "GoogleNative onAdLoaded()")
                        onAdLoaded.invoke()
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Log.i(AdManager.TAG, "GoogleNative onAdClicked()")
                        onAdClicked.invoke()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        Log.i(AdManager.TAG, "GoogleNative onAdImpression()")
                        onAdImpression.invoke()
                    }
                })
                .withNativeAdOptions(
                    nativeAdOptions
                        ?: NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build()
                )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            Log.i(AdManager.TAG, "GoogleNative enable = false or adUnitNull")
        }
    }
}