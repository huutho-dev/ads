package com.sdk.ads.manager

import android.app.Activity
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sdk.ads.AdRequestLoading
import com.sdk.ads.Constants


class InterstitialManager(private val adUnitManager: AdUnitManager) {

    private val listInters = mutableListOf<AdRequestLoading>()

    fun getAdLoadingByType(screenLabel: String, adType: Constants.AdType): AdRequestLoading? {
        return listInters.find { it.screenLabel == screenLabel && it.type == adType }
    }

    fun loadGGInters(
        activity: Activity,
        screenLabel: String,
        onAdLoaded: (interstitialAd: InterstitialAd) -> Unit = {},
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {}
    ) {
        val adRequestLoading = getAdLoadingByType(screenLabel, Constants.AdType.GGInters)

        if (adRequestLoading != null) {
            return
        }

        val adUnit = adUnitManager.findAdUnit(Constants.AdType.GGInters, screenLabel)

        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {

            listInters.add(
                AdRequestLoading(
                    null,
                    true,
                    screenLabel,
                    Constants.AdType.GGInters
                )
            )

            InterstitialAd.load(
                activity,
                adUnit.adUnitId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)

                        getAdLoadingByType(
                            screenLabel,
                            Constants.AdType.GGInters
                        )?.isLoading = false

                        getAdLoadingByType(
                            screenLabel,
                            Constants.AdType.GGInters
                        )?.adObject = interstitialAd

                        onAdLoaded.invoke(interstitialAd)
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)

                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.GGInters
                            )
                        )

                        onAdFailedToLoad.invoke(adError)
                    }
                })
        }

    }

    fun showGGInters(
        activity: Activity,
        screenLabel: String,
        onAdStillLoading : () -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {},
        onAdImpression: () -> Unit = {},
        onAdClicked: () -> Unit = {},
    ) {
        val ad = getAdLoadingByType(screenLabel, Constants.AdType.GGInters)

        if (ad != null && ad.isLoading){
            onAdStillLoading.invoke()
            return
        }

        if (ad != null && !ad.isLoading && ad.adObject != null && ad.adObject is InterstitialAd) {

            (ad.adObject as InterstitialAd).fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)

                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.GGInters
                            )
                        )

                        onAdFailedToShowFullScreenContent.invoke(adError)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        onAdShowedFullScreenContent.invoke()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.GGInters
                            )
                        )

                        onAdDismissedFullScreenContent.invoke()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        onAdImpression.invoke()
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        onAdClicked.invoke()
                    }
                }

            (ad.adObject as InterstitialAd).show(activity)
            return
        }

        onAdFailedToShowFullScreenContent.invoke(AdError(1,"",""))
    }


    fun loadFbInters(
        activity: Activity,
        screenLabel: String,
        onError: (adError: com.facebook.ads.AdError?) -> Unit,
        onAdLoaded: () -> Unit,
        onAdClicked: () -> Unit,
        onLoggingImpression: () -> Unit,
        onInterstitialDisplayed: () -> Unit,
        onInterstitialDismissed: () -> Unit,
    ) {
        val adRequestLoading = getAdLoadingByType(screenLabel, Constants.AdType.FbInters)

        if (adRequestLoading != null) {
            return
        }


        val adUnit = adUnitManager.findAdUnit(Constants.AdType.FbInters, screenLabel)
        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {

            val mFbInters = com.facebook.ads.InterstitialAd(activity, adUnit.adUnitId)

            listInters.add(
                AdRequestLoading(
                    null,
                    true,
                    screenLabel,
                    Constants.AdType.FbInters
                )
            )

            val adConfig = mFbInters.buildLoadAdConfig()
                .withAdListener(object : InterstitialAdListener {
                    override fun onError(ad: Ad?, adError: com.facebook.ads.AdError?) {
                        onError.invoke(adError)

                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.FbInters
                            )
                        )
                    }

                    override fun onAdLoaded(ad: Ad?) {
                        onAdLoaded.invoke()

                        getAdLoadingByType(
                            screenLabel,
                            Constants.AdType.FbInters
                        )?.adObject = mFbInters
                    }

                    override fun onAdClicked(ad: Ad?) {
                        onAdClicked.invoke()
                    }

                    override fun onLoggingImpression(ad: Ad?) {
                        onLoggingImpression.invoke()
                    }

                    override fun onInterstitialDisplayed(ad: Ad?) {
                        onInterstitialDisplayed.invoke()
                    }

                    override fun onInterstitialDismissed(ad: Ad?) {
                        onInterstitialDismissed.invoke()
                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.FbInters
                            )
                        )
                    }
                })
                .build()

            mFbInters.loadAd(adConfig)
        }
    }

    fun showFbInters(screenLabel: String) {
        val ad = getAdLoadingByType(screenLabel, Constants.AdType.GGInters)
        if (ad != null && !ad.isLoading && ad.adObject != null && ad.adObject is com.facebook.ads.InterstitialAd) {
            (ad.adObject as com.facebook.ads.InterstitialAd).show()
        }
    }

}