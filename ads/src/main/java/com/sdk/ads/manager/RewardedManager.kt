package com.sdk.ads.manager

import android.app.Activity
import com.facebook.ads.Ad
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.RewardedVideoAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sdk.ads.AdRequestLoading
import com.sdk.ads.Constants

class RewardedManager(private val adUnitManager: AdUnitManager) {

    private val listInters = mutableListOf<AdRequestLoading>()


    fun getAdLoadingByType(screenLabel: String, adType: Constants.AdType): AdRequestLoading? {
        return listInters.find { it.screenLabel == screenLabel && it.type == adType }
    }

    fun loadGGRewarded(
        activity: Activity,
        screenLabel: String,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdLoaded: (rewardedAd: RewardedAd) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError?) -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {}
    ) {

        val adRequestLoading = getAdLoadingByType(screenLabel, Constants.AdType.GGRewarded)

        if (adRequestLoading != null) {
            return
        }

        val adUnit = adUnitManager.findAdUnit(Constants.AdType.GGRewarded, screenLabel)

        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {

            listInters.add(
                AdRequestLoading(
                    null,
                    true,
                    screenLabel,
                    Constants.AdType.GGRewarded
                )
            )

            RewardedAd.load(activity, adUnit.adUnitId,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        onAdFailedToLoad.invoke(adError)
                        listInters.remove(
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.GGRewarded
                            )
                        )
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        getAdLoadingByType(screenLabel, Constants.AdType.GGRewarded)?.adObject =
                            rewardedAd
                        getAdLoadingByType(screenLabel, Constants.AdType.GGRewarded)?.isLoading =
                            false

                        rewardedAd.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdShowedFullScreenContent() {
                                    onAdShowedFullScreenContent.invoke()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    onAdFailedToShowFullScreenContent.invoke(adError)
                                    listInters.remove(
                                        getAdLoadingByType(
                                            screenLabel,
                                            Constants.AdType.GGRewarded
                                        )
                                    )
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    onAdDismissedFullScreenContent.invoke()
                                    listInters.remove(
                                        getAdLoadingByType(
                                            screenLabel,
                                            Constants.AdType.GGRewarded
                                        )
                                    )
                                }
                            }

                        onAdLoaded.invoke(rewardedAd)
                    }
                })
        }
    }

    fun showGGRewarded(
        activity: Activity,
        screenLabel: String,
        listener: OnUserEarnedRewardListener
    ) {
        val ad = getAdLoadingByType(screenLabel, Constants.AdType.GGRewarded)

        if (ad != null && !ad.isLoading && ad.adObject != null && ad.adObject is RewardedAd) {
            (ad.adObject as RewardedAd).show(activity) {
                listInters.remove(
                    getAdLoadingByType(
                        screenLabel,
                        Constants.AdType.GGRewarded
                    )
                )
                listener.onUserEarnedReward(it)
            }
        }
    }

    fun loadFbRewarded(
        activity: Activity,
        screenLabel: String,
        onError: (error: com.facebook.ads.AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onRewardedVideoCompleted: () -> Unit = {},
        onRewardedVideoClosed: () -> Unit = {},
    ) {
        val adRequestLoading = getAdLoadingByType(screenLabel, Constants.AdType.FbRewarded)
        if (adRequestLoading != null) {
            return
        }

        val adUnit = adUnitManager.findAdUnit(Constants.AdType.FbRewarded, screenLabel)
        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()) {

            listInters.add(AdRequestLoading(null, true, screenLabel, Constants.AdType.FbRewarded))

            val fbRewardedAd = RewardedVideoAd(activity, adUnit.adUnitId)

            fbRewardedAd.loadAd(
                fbRewardedAd.buildLoadAdConfig()
                    .withAdListener(object : RewardedVideoAdListener {
                        override fun onError(ad: Ad?, error: com.facebook.ads.AdError?) {
                            onError.invoke(error)
                            listInters.remove(
                                getAdLoadingByType(
                                    screenLabel,
                                    Constants.AdType.FbRewarded
                                )
                            )
                        }

                        override fun onAdLoaded(ad: Ad?) {
                            onAdLoaded.invoke()
                            getAdLoadingByType(
                                screenLabel,
                                Constants.AdType.FbRewarded
                            )?.isLoading = false
                            getAdLoadingByType(screenLabel, Constants.AdType.FbRewarded)?.adObject =
                                fbRewardedAd
                        }

                        override fun onAdClicked(ad: Ad?) {
                            onAdClicked.invoke()
                        }

                        override fun onLoggingImpression(ad: Ad?) {
                            onLoggingImpression.invoke()
                        }

                        override fun onRewardedVideoCompleted() {
                            onRewardedVideoCompleted.invoke()
                        }

                        override fun onRewardedVideoClosed() {
                            onRewardedVideoClosed.invoke()
                            listInters.remove(
                                getAdLoadingByType(
                                    screenLabel,
                                    Constants.AdType.FbRewarded
                                )
                            )
                        }
                    })
                    .build()
            )
        }
    }

    fun showFbRewarded(screenLabel: String) {
        val ad = getAdLoadingByType(screenLabel, Constants.AdType.FbRewarded)
        if (ad != null && !ad.isLoading && ad.adObject != null && ad.adObject is RewardedVideoAd) {
            (ad.adObject as RewardedVideoAd).show()
        }
    }
}