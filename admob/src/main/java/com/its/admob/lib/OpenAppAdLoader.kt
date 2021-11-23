package com.its.admob.lib

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

class OpenAppAdLoader(
    private val application: Application,
    private val adUnitId: String?,
    private var isForceOpenFirst : Boolean = true
) : Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var loadTime: Long = 0
    private var appOpenAd: AppOpenAd? = null

    private var mLoadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(appOpenAd: AppOpenAd) {
            super.onAdLoaded(appOpenAd)
            this@OpenAppAdLoader.appOpenAd = appOpenAd
            this@OpenAppAdLoader.loadTime = System.currentTimeMillis()

            if (isForceOpenFirst) {
                showAdIfAvailable()
                isForceOpenFirst = false
            }
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            super.onAdFailedToLoad(error)
        }
    }

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
            }

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(currentActivity)
        } else {
            fetchAd()
        }
    }

    private fun fetchAd() {
        if (isAdAvailable())
            return

        if (AdMobLib.isPremium)
            return

        adUnitId ?: return

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            application, adUnitId, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, mLoadCallback
        )
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