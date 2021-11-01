package com.sdk.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.NativeAdLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.gson.Gson
import com.sdk.ads.facebook.FacebookBanner
import com.sdk.ads.facebook.FacebookInterstitial
import com.sdk.ads.facebook.FacebookNative
import com.sdk.ads.facebook.FacebookRewarded
import com.sdk.ads.google.GoogleBanner
import com.sdk.ads.google.GoogleInterstitial
import com.sdk.ads.google.GoogleNative
import com.sdk.ads.google.GoogleRewarded
import com.sdk.ads.google.nativetemplates.TemplateView

object AdManager {

    internal const val TAG = "AdManager"

    private lateinit var adsConfig: AdConfig

    internal var isEnableAd = true

    internal var googleBanner: Banner? = null
    internal var googleInterstitial: Interstitial? = null
    internal var googleNative: Native? = null
    internal var googleRewarded: Rewarded? = null

    private var lastTimeGGShowInterstitial = 0L
    private var lastTimeGGShowRewarded = 0L
    private var ggInterstitialLoading: GoogleInterstitial? = null
    private var ggRewardedLoading: GoogleRewarded? = null


    internal var facebookBanner: Banner? = null
    internal var facebookInterstitial: Interstitial? = null
    internal var facebookNative: Native? = null
    internal var facebookRewarded: Rewarded? = null

    private var lastTimeFbShowInterstitial = 0L
    private var lastTimeFbShowRewarded = 0L
    private var fbInterstitialLoading: FacebookInterstitial? = null
    private var fbRewardedLoading: FacebookRewarded? = null


    private const val Rewarded_Time_Threshold = 1000 * 30 // 1 minute
    private const val Interstitial_Time_Threshold = 1000 * 30 // 1 minute


    private val shouldLoadGGInterstitial: Boolean
        get() = System.currentTimeMillis() - lastTimeGGShowInterstitial > Interstitial_Time_Threshold

    private val shouldLoadGGRewarded: Boolean
        get() = System.currentTimeMillis() - lastTimeGGShowRewarded > Rewarded_Time_Threshold

    private val shouldLoadFbInterstitial: Boolean
        get() = System.currentTimeMillis() - lastTimeFbShowInterstitial > Interstitial_Time_Threshold


    private val shouldLoadFbRewarded: Boolean
        get() = System.currentTimeMillis() - lastTimeFbShowRewarded > Rewarded_Time_Threshold


    @JvmStatic
    fun initialize(context: Context) {
        Log.i(TAG, "initialize")
        MobileAds.initialize(context)
        val testDeviceIds = listOf("31957FBA11123E783152CB17129C6D87")
        AdSettings.addTestDevice("7aca8dad-2377-46aa-b31d-ea5211cc60b7");
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        AudienceNetworkAds.initialize(context)
    }

    @JvmStatic
    @JvmOverloads
    fun setAdsConfig(adsJsonConfig: String?, isPremium: Boolean = false) = try {
        Log.i(TAG, "setAdsConfig ($adsJsonConfig, $isPremium)")
        when {
            isPremium -> {
                isEnableAd = false
            }
            adsJsonConfig.isNullOrBlank() -> {
                isEnableAd = false
            }
            else -> {
                adsConfig = Gson().fromJson(adsJsonConfig, AdConfig::class.java)
                isEnableAd = adsConfig.isEnable == true

                adsConfig.ads?.google?.let {
                    googleBanner = it.banner
                    googleInterstitial = it.interstitial
                    googleNative = it.native
                    googleRewarded = it.rewarded
                }

                adsConfig.ads?.facebook?.let {
                    facebookBanner = it.banner
                    facebookInterstitial = it.interstitial
                    facebookNative = it.native
                    facebookRewarded = it.rewarded
                }

                Log.i(TAG, "setAdsConfig \nadsConfig = $adsConfig\nisEnableAd = $isEnableAd")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.i(TAG, "setAdsConfig (Exception ${e.printStackTrace()} )")
    }

    @JvmOverloads
    @JvmStatic
    fun showGGBanner(
        viewGroup: ViewGroup, adSize: AdSize = AdSize.FULL_BANNER,
        onAdClosed: () -> Unit = {},
        onAdFailedToLoad: (error: LoadAdError) -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        GoogleBanner().showBannerGoogle(
            viewGroup,
            adSize,
            onAdClosed,
            onAdFailedToLoad,
            onAdOpened,
            onAdLoaded,
            onAdClicked,
            onAdImpression
        )
    }

    @JvmOverloads
    @JvmStatic
    fun showGGNative(
        templateView: TemplateView,
        nativeAdOptions: NativeAdOptions? = null,
        onNativeAdLoaded: (ad: NativeAd) -> Boolean = { false },
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdClosed: () -> Unit = {},
        onAdOpened: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onAdImpression: () -> Unit = {},
    ) {
        GoogleNative().showGoogleNative(
            templateView,
            nativeAdOptions,
            onNativeAdLoaded,
            onAdFailedToLoad,
            onAdClosed,
            onAdOpened,
            onAdLoaded,
            onAdClicked,
            onAdImpression
        )
    }

    @JvmStatic
    @JvmOverloads
    fun loadGGInterstitial(
        activity: Activity,
        onAdLoaded: (interstitialAd: InterstitialAd) -> Unit = {},
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {},
        onAdImpression: () -> Unit = {},
        onAdClicked: () -> Unit = {},
    ) {
        if (!shouldLoadGGInterstitial) {
            Log.i(TAG, "shouldLoadGGInterstitial = false")
            return
        }

        ggInterstitialLoading = GoogleInterstitial()
        ggInterstitialLoading!!.loadInterstitial(
            activity,
            onAdLoaded,
            onAdFailedToLoad = {
                ggInterstitialLoading = null
                onAdFailedToLoad.invoke(it)
            },
            onAdFailedToShowFullScreenContent,
            onAdShowedFullScreenContent,
            onAdDismissedFullScreenContent = {
                ggInterstitialLoading = null
                lastTimeGGShowInterstitial = System.currentTimeMillis()
                onAdDismissedFullScreenContent.invoke()
            },
            onAdImpression,
            onAdClicked
        )
    }

    @JvmStatic
    @JvmOverloads
    fun showGGInterstitial(activity: Activity) {
        ggInterstitialLoading?.showInterstitialGoogle(activity)
    }


    @JvmOverloads
    @JvmStatic
    fun loadGGRewarded(
        activity: Activity,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdLoaded: (rewardedAd: RewardedAd) -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
        onAdFailedToShowFullScreenContent: (adError: AdError?) -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {},
    ) {

        if (!shouldLoadGGRewarded) {
            Log.i(TAG, "shouldLoadGGRewarded = false")
            return
        }

        ggRewardedLoading = GoogleRewarded()
        ggRewardedLoading!!.loadRewarded(
            activity,
            onAdFailedToLoad = {
                ggRewardedLoading = null
                onAdFailedToLoad.invoke(it)
            },
            onAdLoaded,
            onAdShowedFullScreenContent,
            onAdFailedToShowFullScreenContent = {
                ggRewardedLoading = null
                onAdFailedToShowFullScreenContent.invoke(it)
            },
            onAdDismissedFullScreenContent = {
                ggRewardedLoading = null
                lastTimeGGShowRewarded = System.currentTimeMillis()
                onAdDismissedFullScreenContent.invoke()
            },
        )
    }

    @JvmOverloads
    @JvmStatic
    fun showGGRewarded(
        activity: Activity,
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit = {},
    ) {
        ggRewardedLoading?.showRewardedAdAfterUserConfirm(
            activity,
            onUserEarnedReward
        )
    }


    @JvmStatic
    @JvmOverloads
    fun showFbBanner(
        viewGroup: ViewGroup,
        adSize: com.facebook.ads.AdSize = com.facebook.ads.AdSize.BANNER_HEIGHT_50,
        onError: () -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {}
    ) {
        FacebookBanner().showBanner(
            viewGroup,
            adSize,
            onError,
            onAdLoaded,
            onAdClicked,
            onLoggingImpression
        )
    }


    @JvmOverloads
    @JvmStatic
    fun loadFbInterstitial(
        activity: Activity,
        onAdLoaded: () -> Unit = {},
        onError: (adError: com.facebook.ads.AdError?) -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onInterstitialDisplayed: () -> Unit = {},
        onInterstitialDismissed: () -> Unit = {},
    ) {

        if (!shouldLoadFbInterstitial) {
            Log.i(TAG, "shouldLoadFbInterstitial = false")
            return
        }

        fbInterstitialLoading = FacebookInterstitial()
        fbInterstitialLoading!!.loadInterstitial(
            activity,
            onError = {
                fbInterstitialLoading = null
                onError.invoke(it)
            },
            onAdLoaded,
            onAdClicked,
            onLoggingImpression,
            onInterstitialDisplayed,
            onInterstitialDismissed = {
                lastTimeFbShowInterstitial = System.currentTimeMillis()
                fbInterstitialLoading = null
                onInterstitialDismissed.invoke()
            }
        )
    }


    @JvmStatic
    fun showFbInterstitial() {
        fbInterstitialLoading?.showInterstitial()
    }


    @JvmStatic
    @JvmOverloads
    fun showFbNative(
        nativeAdLayout: NativeAdLayout,
        onError: (error: com.facebook.ads.AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onMediaDownloaded: () -> Unit = {},
    ) {
        FacebookNative().loadNative(
            nativeAdLayout,
            onError,
            onAdLoaded,
            onAdClicked,
            onLoggingImpression,
            onMediaDownloaded
        )
    }


    @JvmOverloads
    @JvmStatic
    fun loadFbRewarded(
        activity: Activity,
        onError: (error: com.facebook.ads.AdError?) -> Unit = {},
        onAdLoaded: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onRewardedVideoCompleted: () -> Unit = {},
        onRewardedVideoClosed: () -> Unit = {},
    ) {
        if (!shouldLoadFbRewarded) {
            Log.i(TAG, "shouldLoadFbRewarded = false")
            return
        }

        fbRewardedLoading = FacebookRewarded()
        fbRewardedLoading!!.loadRewarded(
            activity,
            onError = {
                fbRewardedLoading = null
                onError.invoke(it)
            },
            onAdLoaded,
            onAdClicked,
            onLoggingImpression,
            onRewardedVideoCompleted,
            onRewardedVideoClosed = {
                lastTimeFbShowRewarded = System.currentTimeMillis()
                fbRewardedLoading = null
                onRewardedVideoClosed.invoke()
            }
        )
    }


    @JvmStatic
    fun showFbReward() {
        fbRewardedLoading?.showRewarded()
    }


    @JvmStatic
    @JvmOverloads
    fun showBanner(
        viewGroup: ViewGroup,
        adSizeFb: com.facebook.ads.AdSize = com.facebook.ads.AdSize.BANNER_HEIGHT_50,
        adSizeGG: AdSize = AdSize.FULL_BANNER
    ) {
        showFbBanner(
            viewGroup,
            adSizeFb,
            onError = { showGGBanner(viewGroup, adSizeGG) },
            onAdLoaded = {},
            onAdClicked = {},
            onLoggingImpression = {}
        )
    }


    @JvmStatic
    fun showNative(fbNativeLayout: NativeAdLayout, ggTemplateView: TemplateView) {
        showFbNative(fbNativeLayout, onError = {
            showGGNative(ggTemplateView)
        })
    }

    @JvmOverloads
    @JvmStatic
    fun loadInterstitial(
        activity: Activity,
        onAdLoaded: () -> Unit = {},
        onError: () -> Unit = {},
        onAdClicked: () -> Unit = {},
        onLoggingImpression: () -> Unit = {},
        onInterstitialDisplayed: () -> Unit = {},
        onInterstitialDismissed: () -> Unit = {},
    ) {
        when {
            shouldLoadFbInterstitial -> {
                loadFbInterstitial(
                    activity,
                    onAdLoaded = { onAdLoaded.invoke() },
                    onError = {
                        onError.invoke()
                        loadGGInterstitial(
                            activity,
                            { onAdLoaded.invoke() },
                            { onError.invoke() }
                        )
                    },
                    onAdClicked,
                    onLoggingImpression,
                    onInterstitialDisplayed,
                    onInterstitialDismissed
                )
            }
            shouldLoadGGInterstitial -> {
                loadGGInterstitial(
                    activity,
                    { onAdLoaded.invoke() },
                    { onError.invoke() }
                )
            }
            else -> {
                Log.i(TAG, "shouldLoadFbInterstitial == false && shouldLoadGGInterstitial == false")
            }
        }
    }


    @JvmStatic
    fun showInterstitial(activity: Activity) {
        if (fbInterstitialLoading != null) {
            fbInterstitialLoading?.showInterstitial()
        } else if (ggInterstitialLoading != null) {
            ggInterstitialLoading?.showInterstitialGoogle(activity)
        }
    }


    @JvmStatic
    @JvmOverloads
    fun loadRewarded(activity: Activity, onAdLoaded: () -> Unit = {}) {
        when {
            shouldLoadFbRewarded -> {
                loadFbRewarded(
                    activity,
                    onError = {
                        loadGGRewarded(
                            activity,
                            onAdLoaded = {
                                onAdLoaded.invoke()
                            }
                        )
                    },
                    onAdLoaded = onAdLoaded
                )
            }
            shouldLoadGGRewarded -> {
                loadGGRewarded(
                    activity,
                    onAdLoaded = {
                        onAdLoaded.invoke()
                    }
                )
            }
            else -> {
                Log.i(TAG, "shouldLoadFbRewarded == false && shouldLoadGGRewarded == false")
            }
        }

    }


    @JvmOverloads
    @JvmStatic
    fun showRewarded(
        activity: Activity,
        onGGUserEarnedReward: (rewardItem: RewardItem) -> Unit = {}
    ) {
        if (fbRewardedLoading != null) {
            showFbReward()
        } else if (ggRewardedLoading != null) {
            showGGRewarded(activity, onGGUserEarnedReward)
        }
    }

}