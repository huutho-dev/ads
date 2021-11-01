package com.sdk.ads.facebook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.ads.*
import com.sdk.ads.AdManager


class FacebookNative {
    private var nativeAd: NativeAd? = null

    fun loadNative(
        nativeAdLayout: NativeAdLayout,
        onError: (error: AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onMediaDownloaded: () -> Unit = {},
    ) {
        Log.i(AdManager.TAG, "FacebookNative loadNative")
        if (AdManager.isEnableAd
            && AdManager.facebookNative?.isEnable == true
            && !AdManager.facebookNative?.adUnitId.isNullOrBlank()
        ) {
            nativeAdLayout.visibility = View.GONE
            nativeAd = NativeAd(nativeAdLayout.context, AdManager.facebookNative?.adUnitId)
            nativeAd!!.loadAd(
                nativeAd!!.buildLoadAdConfig()
                    .withAdListener(object : NativeAdListener {
                        override fun onError(ad: Ad?, error: AdError?) {
                            Log.i(AdManager.TAG, "FacebookNative onError = ${error?.errorMessage}")
                            onError.invoke(error)
                        }

                        override fun onAdLoaded(ad: Ad?) {
                            Log.i(AdManager.TAG, "FacebookNative onAdLoaded = ${ad?.placementId}")
                            onAdLoaded.invoke()
                            if (nativeAd == null || nativeAd != ad) {
                                return
                            }
                            nativeAdLayout.visibility = View.VISIBLE
                            inflateAd(nativeAdLayout, nativeAd!!)

                        }

                        override fun onAdClicked(ad: Ad?) {
                            Log.i(AdManager.TAG, "FacebookNative onAdClicked = ${ad?.placementId}")
                            onAdClicked.invoke()
                        }

                        override fun onLoggingImpression(ad: Ad?) {
                            Log.i(
                                AdManager.TAG,
                                "FacebookNative onLoggingImpression = ${ad?.placementId}"
                            )
                            onLoggingImpression.invoke()
                        }

                        override fun onMediaDownloaded(ad: Ad?) {
                            Log.i(
                                AdManager.TAG,
                                "FacebookNative onMediaDownloaded = ${ad?.placementId}"
                            )
                            onMediaDownloaded.invoke()
                        }
                    })
                    .build()
            );
        } else {
            Log.i(AdManager.TAG, "FacebookNative enable = false or adUnitNull")
        }

    }

    private fun inflateAd(nativeAdLayout: NativeAdLayout, nativeAd: NativeAd) {
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