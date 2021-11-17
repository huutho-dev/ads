package com.sdk.ads.manager

import com.sdk.ads.Constants

class TimeManager(var delayShowTime: Long = 0) {

    private val timeMap = mutableMapOf<String, Long>()

    fun saveTimeLoad(screenLabel: String, adType: Constants.AdType) {
        val key = adType.name.plus("#").plus(screenLabel)
        val value = System.currentTimeMillis()
        timeMap[key] = value
    }

    fun canLoadAd(screenLabel: String, adType: Constants.AdType): Boolean {
        val key = adType.name.plus("#").plus(screenLabel)
        val value = timeMap[key] ?: 0
        return System.currentTimeMillis() - value > delayShowTime
    }
}