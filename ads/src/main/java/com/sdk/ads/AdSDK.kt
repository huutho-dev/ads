package com.sdk.ads

import android.annotation.SuppressLint
import android.app.Application
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.sdk.ads.manager.banner.BannerManager
import com.sdk.ads.manager.inters.IntersManager
import com.sdk.ads.manager.nativeAd.NativeAdListener
import com.sdk.ads.manager.nativeAd.NativeManager
import com.sdk.ads.manager.open.OpenAppAd
import com.sdk.ads.manager.rewarded.RewardedManager
import com.sdk.ads.template.google.nativetemplates.TemplateView
import org.json.JSONArray
import org.json.JSONObject

const val bannerTag = "BannerAd"
const val intersTag = "IntersAd"
const val nativeTag = "NativeAd"
const val rewardedTag = "RewardedAd"
const val openAppTag = "OpenAppAd"

object AdSDK {
    lateinit var application: Application
    internal var isPremium = false
    internal var isEnable = true
    internal var timeDelayToLoad = 0L

    private lateinit var banner: BannerManager
    private lateinit var native: NativeManager
    private lateinit var interstitial: IntersManager
    private lateinit var rewarded: RewardedManager

    @SuppressLint("StaticFieldLeak")
    private lateinit var open: OpenAppAd

    private val openMap = mutableMapOf<String, String>()
    private val bannerMap = mutableMapOf<String, String>()
    private val nativeMap = mutableMapOf<String, String>()
    private val rewardedMap = mutableMapOf<String, String>()
    private val interstitialMap = mutableMapOf<String, String>()

    fun init(application: Application) {
        AdSDK.application = application
    }

    fun setAdConfig(jsonConfig: String, isPremium: Boolean) {
        AdSDK.isPremium = isPremium
        parseJsonConfig(jsonConfig)

        banner = BannerManager(bannerMap)
        native = NativeManager(nativeMap)
        interstitial = IntersManager(interstitialMap)
        rewarded = RewardedManager(rewardedMap)
        open = OpenAppAd(application, openMap["openApp"])
    }

    private fun parseJsonConfig(jsonConfig: String) {

        fun parseKeyValueAdUnitId(adUnitJsonArray: JSONArray): Map<String, String> {
            val adUnits = mutableMapOf<String, String>()
            for (i in 0 until adUnitJsonArray.length()) {
                val obj: JSONObject = adUnitJsonArray.get(i) as JSONObject
                val valid = obj.has("key") && obj.has("value")
                if (valid) {
                    adUnits[obj.getString("key")] = obj.getString("value")
                }
            }
            return adUnits
        }

        val response = JSONObject(jsonConfig)
        isEnable = if (response.has("isEnable")) response.getBoolean("isEnable") else true
        timeDelayToLoad =
            if (response.has("timeDelayToLoad")) response.getLong("timeDelayToLoad") else 0
        if (response.has("ads")) {
            val ads = response.getJSONObject("ads")
            val open = ads.getJSONArray("open")
            val banner = ads.getJSONArray("banner")
            val native = ads.getJSONArray("native")
            val rewarded = ads.getJSONArray("rewarded")
            val interstitial = ads.getJSONArray("interstitial")

            openMap.clear()
            bannerMap.clear()
            rewardedMap.clear()
            nativeMap.clear()
            interstitialMap.clear()

            openMap.putAll(parseKeyValueAdUnitId(open))
            bannerMap.putAll(parseKeyValueAdUnitId(banner))
            nativeMap.putAll(parseKeyValueAdUnitId(native))
            rewardedMap.putAll(parseKeyValueAdUnitId(rewarded))
            interstitialMap.putAll(parseKeyValueAdUnitId(interstitial))
        }
    }


    @JvmStatic
    @JvmOverloads
    fun showBanner(key: String, viewGroup: ViewGroup, bannerSize: AdSize = AdSize.FULL_BANNER) {
        banner.showBanner(key, viewGroup, bannerSize)
    }

    @JvmStatic
    @JvmOverloads
    fun showNative(
        key: String,
        adTemplateView: TemplateView,
        useCustomLayout: Boolean = false,
        nativeAdListener: NativeAdListener = object : NativeAdListener {}
    ) {
        native.showNativeAd(key, adTemplateView, useCustomLayout, nativeAdListener)
    }


    @JvmStatic
    @JvmOverloads
    fun AppCompatActivity.loadInterstitial(
        key: String,
        onAdLoaded: () -> Unit = {},
        onAdLoadFailure: (errorCode: String) -> Unit = {}
    ) {
        interstitial.loadInter(this, key, onAdLoaded, onAdLoadFailure)
    }

    @JvmStatic
    @JvmOverloads
    fun showInterstitial(
        key: String,
        onAdDismissed: () -> Unit = {},
        onAdFailedToShow: () -> Unit = {}
    ) {
        interstitial.showInterAd(key, onAdDismissed, onAdFailedToShow)
    }


    @JvmStatic
    @JvmOverloads
    fun AppCompatActivity.loadRewarded(
        key: String,
        onAdLoaded: () -> Unit = {},
        onAdLoadFailure: (errorCode: String) -> Unit = {}
    ) {
        rewarded.loadReward(this, key, onAdLoaded, onAdLoadFailure)
    }

    @JvmStatic
    @JvmOverloads
    fun showRewarded(
        key: String,
        onAdDismissed: () -> Unit = {},
        onAdFailedToShow: () -> Unit = {}
    ) {
        rewarded.showReward(key, onAdDismissed, onAdFailedToShow)
    }
}