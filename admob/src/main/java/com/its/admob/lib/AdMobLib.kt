package com.its.admob.lib

import android.app.Application

object AdMobLib {

    enum class AdShowCode {
        LOADING,
        RELOAD,
        WAITING,
        FAILED,
        DISMISS,
    }

    internal lateinit var application: Application
    internal var isPremium: Boolean = false
    internal var interstitialAdUnitId: String = ""
    internal var rewardedAdUnitId: String = ""
    internal var timeDelayToLoad: Long = 0L

    fun init(application: Application, isPremium: Boolean = false) {
        this.application = application
        this.isPremium = isPremium

    }

    fun setInterstitialUnitId(unitId: String, timeDelayToLoad: Long = 0) {
        this.timeDelayToLoad = timeDelayToLoad
        interstitialAdUnitId = unitId
        InterstitialAdLoader.loadInterstitial()
    }

    fun setRewardedUnitId(unitId: String) {
        rewardedAdUnitId = unitId
    }

    fun registerOpenAppAd(adUnitId: String) {
        OpenAppAdLoader(application, adUnitId)
    }
}