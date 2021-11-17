package com.sdk.ads.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.sdk.ads.AdManager
import com.sdk.ads.Constants

class OpenAppManager(
    private val application: Application,
    private val adUnitManager: AdUnitManager
): Application.ActivityLifecycleCallbacks, LifecycleObserver {


    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var loadTime: Long = 0
    private var appOpenAd: AppOpenAd? = null

    private var mLoadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(appOpenAd: AppOpenAd) {
            super.onAdLoaded(appOpenAd)
            this@OpenAppManager.appOpenAd = appOpenAd
            this@OpenAppManager.loadTime = System.currentTimeMillis()

            if (forceFirstTime){
                showAdIfAvailable()
                forceFirstTime = false
            }
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            super.onAdFailedToLoad(error)
        }
    }
    private var forceFirstTime = true

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    /** LifecycleObserver methods  */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {

            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    isShowingAd = false
                    appOpenAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    isShowingAd = true
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    isShowingAd = false
                    appOpenAd = null
                    showAdIfAvailable()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }
            }

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(currentActivity)
        } else {
            fetchAd()
        }
    }

    private fun fetchAd() {
        if (isAdAvailable()) {
            return
        }

        if (!AdManager.isEnableAd) {
            return
        }

        val adUnit = adUnitManager.findAdUnit(Constants.AdType.GGOpenApp, Constants.AdType.GGOpenApp.name)

        if (adUnit?.isEnableUnit == true && adUnit.adUnitId.isNotBlank()){
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                application, adUnit.adUnitId, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, mLoadCallback
            )
        }
    }

    private fun isAdAvailable() = appOpenAd != null
            && System.currentTimeMillis() - loadTime < 4 * 1000 * 60 * 60


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

}