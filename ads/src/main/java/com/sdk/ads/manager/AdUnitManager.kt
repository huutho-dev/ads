package com.sdk.ads.manager

import android.util.Log
import com.sdk.ads.Constants
import org.json.JSONObject

class AdUnitManager(jsonConfig: String, private val isPremium: Boolean) {

    private var mAdsInformation: AdsInformation

    init {
        mAdsInformation = parseAdJsonConfig(jsonConfig)
    }


    fun findAdUnit(adType: Constants.AdType, screenLabel: String): AdUnit? {
        if (isPremium) {
            Log.i("AdUnitManager", "isPremium: $isPremium")
            return null
        }
        if (!mAdsInformation.enable) {
            Log.i("AdUnitManager", "mAdsInformation enable: false")
            return null
        }

        return mAdsInformation.adUnits.find {
            it.adType == adType && it.screenLabel == screenLabel
        }
    }

    fun isEnableAd() = mAdsInformation.enable && !isPremium

    /**
     * Parse json ads from @param adJsonConfig
     *  {
    "isEnable": true,
    "useFirstUnitId": false,
    "ads": [
    "GGBanner#MainFragment#ca-app-pub-3940256099942544/6300978111#enable",
    "GGInterstitial#MainFragment#ca-app-pub-3940256099942544/1033173712#enable",
    "GGRewarded#MainFragment#ca-app-pub-3940256099942544/5224354917#enable",
    "GGNative#MainFragment#ca-app-pub-3940256099942544/2247696110#enable",
    "GGOpenApp#MainFragment#ca-app-pub-3940256099942544/2247696110#enable",
    "FbBanner#MainFragment#233072595555793_233085135554539#enable",
    "FbInterstitial#MainFragment#233072595555793_233084782221241#enable",
    "FbRewarded#MainFragment#233072595555793_233085135554539#enable",
    "FbNative#MainFragment#233072595555793_233084965554556#enable"
    ]
    }
     */
    private fun parseAdJsonConfig(adJsonConfig: String): AdsInformation {
        val adUnits = mutableListOf<AdUnit>()
        val jsonObject = JSONObject(adJsonConfig)

        val isEnableAds =
            if (jsonObject.has("isEnableAds")) jsonObject.getBoolean("isEnableAds")
            else true

        val useFirstUnitId =
            if (jsonObject.has("useFirstUnitId")) jsonObject.getBoolean("useFirstUnitId")
            else false

        val ads = jsonObject.getJSONArray("ads")

        for (i in 0 until ads.length()) {
            val item: String = ads.get(i).toString()
            val split = item.split("#")

            val adType = getAdType(split[0])
            val screenLabel =
                if (adType == Constants.AdType.GGOpenApp) Constants.AdType.GGOpenApp.name else split[1]
            val adUnitId = split[2]
            val enableUnit = getAdUnitActive(split[3])
            val adUnit = AdUnit(adType, screenLabel, adUnitId, enableUnit)
            adUnits.add(adUnit)
        }

        return AdsInformation(isEnableAds, useFirstUnitId, adUnits)
    }

    private fun getAdType(source: String): Constants.AdType {
        return when (source.lowercase()) {
            Constants.AdType.GGBanner.name.lowercase() -> Constants.AdType.GGBanner
            Constants.AdType.GGInters.name.lowercase() -> Constants.AdType.GGInters
            Constants.AdType.GGRewarded.name.lowercase() -> Constants.AdType.GGRewarded
            Constants.AdType.GGNative.name.lowercase() -> Constants.AdType.GGNative
            Constants.AdType.GGOpenApp.name.lowercase() -> Constants.AdType.GGOpenApp
            Constants.AdType.FbBanner.name.lowercase() -> Constants.AdType.FbBanner
            Constants.AdType.FbInters.name.lowercase() -> Constants.AdType.FbInters
            Constants.AdType.FbRewarded.name.lowercase() -> Constants.AdType.FbRewarded
            Constants.AdType.FbNative.name.lowercase() -> Constants.AdType.FbNative
            else -> Constants.AdType.Unknown
        }
    }


    private fun getAdUnitActive(source: String): Boolean {
        return when (source.lowercase()) {
            Constants.AdActive.Disable.name.lowercase() -> false
            else -> true
        }
    }
}

data class AdUnit(
    val adType: Constants.AdType,
    val screenLabel: String,
    val adUnitId: String,
    val isEnableUnit: Boolean = true,
)

data class AdsInformation(
    val enable: Boolean,
    val useFirstUnitId: Boolean,
    val adUnits: List<AdUnit>
)