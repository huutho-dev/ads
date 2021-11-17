package com.sdk.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.*
import com.sdk.ads.AdManager.loadGGInters
import com.sdk.ads.google.nativetemplates.TemplateView
import com.sdk.ads.manager.*

object AdManager {

    private lateinit var mTimeManager: TimeManager
    private lateinit var mApplication: Application
    private lateinit var mAdUnitManager: AdUnitManager
    private lateinit var mBannerManager: BannerManager
    private lateinit var mIntersManager: InterstitialManager
    private lateinit var mNativeManager: NativeManager
    private lateinit var mRewardedManager: RewardedManager

    @SuppressLint("StaticFieldLeak")
    private lateinit var mOpenAppManager: OpenAppManager


    internal const val TAG = "AdManager"

    internal var isEnableAd = true


    @JvmStatic
    fun initialize(context: Application) {
        mApplication = context

        MobileAds.initialize(context)
        val testDeviceIds = listOf("31957FBA11123E783152CB17129C6D87")
        AdSettings.addTestDevice("7aca8dad-2377-46aa-b31d-ea5211cc60b7");
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        AudienceNetworkAds.initialize(context)
    }

    @JvmStatic
    @JvmOverloads
    fun setAdsConfig(adsJsonConfig: String, isPremium: Boolean = false, delayShowTime: Long = 0) {
        isEnableAd = !isPremium
        mTimeManager = TimeManager(delayShowTime)
        mAdUnitManager = AdUnitManager(adsJsonConfig, isPremium)
        mBannerManager = BannerManager(mAdUnitManager)
        mIntersManager = InterstitialManager(mAdUnitManager)
        mNativeManager = NativeManager(mAdUnitManager)
        mRewardedManager = RewardedManager(mAdUnitManager)
        mOpenAppManager = OpenAppManager(mApplication, mAdUnitManager)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.showGGBanner(viewGroup: ViewGroup, adSize: AdSize = AdSize.FULL_BANNER) {
        mBannerManager.showGGBanner(viewGroup, this.javaClass.simpleName, adSize)
    }

    @JvmStatic
    @JvmOverloads
    fun Activity.showGGBanner(viewGroup: ViewGroup, adSize: AdSize = AdSize.FULL_BANNER) {
        mBannerManager.showGGBanner(viewGroup, this.javaClass.simpleName, adSize)
    }


    @JvmStatic
    fun Activity.showGGNative(templateView: TemplateView) {
        mNativeManager.showGGNative(templateView, this.javaClass.simpleName)
    }

    @JvmStatic
    fun Fragment.showGGNative(templateView: TemplateView) {
        mNativeManager.showGGNative(templateView, this.javaClass.simpleName)
    }


    @JvmStatic
    fun Activity.loadGGInters() {
        val label = this.javaClass.simpleName;
        val canLoadAd = mTimeManager.canLoadAd(label, Constants.AdType.GGInters)
        if (canLoadAd) {
            mIntersManager.loadGGInters(
                this,
                label,
                onAdDismissedFullScreenContent = {
                    mTimeManager.saveTimeLoad(label, Constants.AdType.GGInters)
                })
        }
    }

    @JvmStatic
    fun Activity.showGGInters() {
        mIntersManager.showGGInters(this, this.javaClass.simpleName)
    }


    @JvmOverloads
    @JvmStatic
    fun Activity.loadGGRewarded(
        onAdLoaded: () -> Unit = {},
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {}
    ) {
        val label = this.javaClass.simpleName

        fun loadGgRewarded(){
            mRewardedManager.loadGGRewarded(
                this,
                this.javaClass.simpleName,
                onAdLoaded = { onAdLoaded.invoke() },
                onAdFailedToLoad = { onAdFailedToLoad.invoke(it) },
                onAdDismissedFullScreenContent = {
                    mTimeManager.saveTimeLoad(label, Constants.AdType.GGRewarded)
                }
            )
        }

        val canLoadAd = mTimeManager.canLoadAd(label, Constants.AdType.GGRewarded)
        if (canLoadAd) {
            loadGgRewarded()
        }
    }

    @JvmStatic
    fun Activity.showGGRewardedAfterAskUser(onUserEarnedRewardListener: OnUserEarnedRewardListener? = null) {
        mRewardedManager.showGGRewarded(
            this,
            this.javaClass.simpleName,
            onUserEarnedRewardListener ?: OnUserEarnedRewardListener { })
    }
}