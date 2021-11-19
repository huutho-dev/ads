package com.sdk.ads.manager

import com.sdk.ads.AdSDK

abstract class AdManager<T : AdLoading> (private  val adUnitIdMap : MutableMap<String, String>) {

    private var loadAdDelayTime = AdSDK.timeDelayToLoad

    /**
     * Store Activity loading ads
     * [key-activity]
     */
    private val adMap = mutableMapOf<String, T>()

    /**
     * Store last time show Ads by key
     * [key-lastTimeAdShow]
     */
    private val adLastTimeLoadedMap = mutableMapOf<String, Long>()


    /**
     * Return canLoad Ad
     * Time valid
     * && Not Premium
     * && activity not exist
     * && adUnitId not null or blank
     */
    fun checkCanLoadAd(
        key: String,
        onCanLoad: () -> Unit = {},
        onCanNotLoad: (code: String) -> Unit = {}
    ) {
        if (AdSDK.isPremium)
            return

        if (!AdSDK.isEnable) {
            return
        }

        if (!checkTimeValid(key)) {
            onCanNotLoad.invoke(LOAD_TIME_INVALID_ERROR)
            return
        }

        if (adUnitIdMap[key].isNullOrBlank()) {
            onCanNotLoad.invoke(AD_UNIT_INVALID_ERROR)
            return
        }

        if (getAdLoadingByKey(key) != null && getAdLoadingByKey(key)?.isLoading() == true) {
            onCanNotLoad.invoke(AD_STILL_LOADING_ERROR)
            return
        }

        onCanLoad.invoke()
    }

    fun findAdUnitId(key: String): String? {
        return adUnitIdMap[key]
    }

    fun saveAdShowTime(key: String) {
        adLastTimeLoadedMap[key] = System.currentTimeMillis()
        clearInterAdLoading(key)
    }

    fun saveInterAdLoading(key: String, ad: T) {
        adMap[key] = ad
    }

    fun clearInterAdLoading(key: String) {
        adMap.remove(key)
    }

    fun getAdLoadingByKey(key: String): T? {
        return adMap[key]
    }

    private fun checkTimeValid(key: String): Boolean {
        val lastTimeShown = adLastTimeLoadedMap[key]
        return System.currentTimeMillis() - (lastTimeShown ?: 0) >= loadAdDelayTime
    }

    companion object {
        const val LOAD_TIME_INVALID_ERROR = "LOAD_TIME_INVALID_ERROR"
        const val AD_UNIT_INVALID_ERROR = "AD_UNIT_INVALID_ERROR"
        const val AD_STILL_LOADING_ERROR = "AD_STILL_LOADING_ERROR"
    }
}