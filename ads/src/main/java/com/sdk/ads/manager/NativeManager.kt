package com.sdk.ads.manager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.sdk.ads.AdManager
import com.sdk.ads.Constants
import com.sdk.ads.google.nativetemplates.NativeTemplateStyle
import com.sdk.ads.google.nativetemplates.TemplateView

class NativeManager(private val adUnitManager: AdUnitManager) {

    fun showGGNative(
        templateView: TemplateView,
        screenLabel: String,
        nativeAdOptions: NativeAdOptions? = null,
        onNativeAdLoaded: (ad: NativeAd) -> Boolean = { false },
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdClosed: () -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        templateView.visibility = View.GONE
        val adUnit = adUnitManager.findAdUnit(Constants.AdType.GGNative, screenLabel)

        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {
            var adLoader: AdLoader? = null
            adLoader = AdLoader.Builder(templateView.context, adUnit.adUnitId)
                .forNativeAd { ad: NativeAd ->
                    Log.i(AdManager.TAG, "GoogleNative forNativeAd")
                    if (onNativeAdLoaded.invoke(ad)) {
                        return@forNativeAd
                    }

                    if (ViewTreeLifecycleOwner.get(templateView)?.lifecycle?.currentState == Lifecycle.State.DESTROYED) {
                        ad.destroy()
                        return@forNativeAd
                    }

                    if (adLoader?.isLoading == false) {
                        val styles = NativeTemplateStyle.Builder()
                            .withMainBackgroundColor(ColorDrawable(Color.TRANSPARENT))
                            .build()
                        templateView.setStyles(styles)
                        templateView.setNativeAd(ad)
                        templateView.visibility = View.VISIBLE
                    }
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i(AdManager.TAG, "GoogleNative onAdFailedToLoad(${adError.message})")
                        onAdFailedToLoad.invoke(adError)
                        templateView.visibility = View.GONE
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
        }
    }


    fun showFbNative(
        nativeAdLayout: NativeAdLayout,
        screenLabel: String,
        onError: (error: AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onMediaDownloaded: () -> Unit = {},
    ) {
        nativeAdLayout.visibility = View.GONE
        val adUnit = adUnitManager.findAdUnit(Constants.AdType.FbNative, screenLabel)
        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {
            com.facebook.ads.NativeAd(nativeAdLayout.context, adUnit.adUnitId).let { nativeAd ->
                nativeAd.loadAd(
                    nativeAd.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onError(ad: Ad?, error: AdError?) {
                                onError.invoke(error)
                            }

                            override fun onAdLoaded(ad: Ad?) {
                                onAdLoaded.invoke()
                                nativeAdLayout.visibility = View.VISIBLE
                                inflateFbNativeAd(nativeAdLayout, nativeAd)
                            }

                            override fun onAdClicked(ad: Ad?) {
                                onAdClicked.invoke()
                            }

                            override fun onLoggingImpression(ad: Ad?) {
                                onLoggingImpression.invoke()
                            }

                            override fun onMediaDownloaded(ad: Ad?) {
                                onMediaDownloaded.invoke()
                            }
                        })
                        .build()
                )
            }
        }
    }

    private fun inflateFbNativeAd(nativeAdLayout: NativeAdLayout, nativeAd: com.facebook.ads.NativeAd) {
        val context = nativeAdLayout.context
        nativeAd.unregisterView()

        val inflater = LayoutInflater.from(context)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adView = inflater.inflate(
            com.sdk.ads.R.layout.native_ad_layout,
            nativeAdLayout,
            false
        ) as LinearLayout
        nativeAdLayout.addView(adView)

        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout =
            adView.findViewById(com.sdk.ads.R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon: MediaView = adView.findViewById(com.sdk.ads.R.id.native_ad_icon)
        val nativeAdTitle: TextView = adView.findViewById(com.sdk.ads.R.id.native_ad_title)
        val nativeAdMedia: MediaView = adView.findViewById(com.sdk.ads.R.id.native_ad_media)
        val nativeAdSocialContext: TextView =
            adView.findViewById(com.sdk.ads.R.id.native_ad_social_context)
        val nativeAdBody: TextView = adView.findViewById(com.sdk.ads.R.id.native_ad_body)
        val sponsoredLabel: TextView =
            adView.findViewById(com.sdk.ads.R.id.native_ad_sponsored_label)
        val nativeAdCallToAction: Button =
            adView.findViewById(com.sdk.ads.R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews
        )
    }
}