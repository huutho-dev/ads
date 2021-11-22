package com.its.admob.lib

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.ads.mediation.facebook.FacebookAdapter
import com.google.ads.mediation.facebook.FacebookExtras
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.its.admob.lib.nativeView.google.tem.GGNativeStyle
import com.its.admob.lib.nativeView.google.tem.GGNativeView


object NativeAdLoader {

    private const val nativeTag = "NativeAd"

    const val HOME_FRAGMENT_ID = "ca-app-pub-3940256099942544/2247696110"
    const val TOOLS_FRAGMENT_ID = "ca-app-pub-3940256099942544/2247696110"
    const val FILE_ADAPTER_ID = "ca-app-pub-3940256099942544/2247696110"

    @JvmStatic
    @JvmOverloads
    fun addNative(
        activity: FragmentActivity?,
        templateView : GGNativeView,
        adUnitId: String,
        useCustomNativeLayout: Boolean = false,
        onAdClosed: () -> Unit = {},
        onAdFailedToLoad: () -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
        onBuildNativeAd: (ad: NativeAd, useCustomNativeLayout: Boolean) -> Unit = { _, _ -> },
    ) {
        activity?:return
        templateView.visibility = View.GONE

        if (AdMobLib.isPremium)
            return

        val activityLifeCycleObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                templateView.destroyNativeAd()
                activity.lifecycle.removeObserver(this)
            }
        }

        activity.lifecycle.addObserver(activityLifeCycleObserver)

        fun buildNativeAd(ad: NativeAd) {
            if (activity.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                Log.i(nativeTag, "onBuildNativeAd() # Lifecycle.State.DESTROYED ad")
                ad.destroy()
                return
            }
            templateView.visibility = View.VISIBLE
            if (useCustomNativeLayout) {
                Log.i(nativeTag, "onBuildNativeAd() # use CUSTOM native layout")
                onBuildNativeAd.invoke(ad, true)
            } else {
                val styles = GGNativeStyle.Builder()
                    .withMainBackgroundColor(ColorDrawable(Color.TRANSPARENT))
                    .build()

                templateView.setStyles(styles)
                templateView.setNativeAd(ad)

                Log.i(nativeTag, "onBuildNativeAd() # use DEFAULT native layout")
                onBuildNativeAd(ad, false)
            }
        }

        val adLoader: AdLoader = AdLoader.Builder(activity, adUnitId)
            .forNativeAd { ad: NativeAd -> buildNativeAd(ad) }
            .withAdListener(object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.i(nativeTag, "onNativeClosed()")
                    onAdClosed.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.i(nativeTag, "onNativeFailedToLoad() ${loadAdError.message}")
                    onAdFailedToLoad.invoke()
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.i(nativeTag, "onNativeOpened()")
                    onAdOpened.invoke()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i(nativeTag, "onNativeLoaded()")
                    onAdLoaded.invoke()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.i(nativeTag, "onNativeClicked()")
                    onAdClicked.invoke()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i(nativeTag, "onNativeImpression()")
                    onAdImpression.invoke()
                }
            })
            .build()

        adLoader.loadAd(
            AdRequest.Builder()
                // https://developers.google.com/admob/android/mediation/facebook#kotlin_1
                .addNetworkExtrasBundle(
                    FacebookAdapter::class.java,
                    FacebookExtras().setNativeBanner(true).build()
                )
                .build()
        )
    }
}