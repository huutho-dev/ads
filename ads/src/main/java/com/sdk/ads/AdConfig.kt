package com.sdk.ads
import com.google.gson.annotations.SerializedName

data class AdConfig(
    @SerializedName("ads")
    val ads: Ads?,
    @SerializedName("isEnable")
    val isEnable: Boolean? // true
)

data class Ads(
    @SerializedName("facebook")
    val facebook: Facebook?,
    @SerializedName("google")
    val google: Google?
)

data class Google(
    @SerializedName("banner")
    val banner: Banner?,
    @SerializedName("interstitial")
    val interstitial: Interstitial?,
    @SerializedName("native")
    val native: Native?,
    @SerializedName("rewarded")
    val rewarded: Rewarded?,
)

data class Facebook(
    @SerializedName("banner")
    val banner: Banner?,
    @SerializedName("interstitial")
    val interstitial: Interstitial?,
    @SerializedName("native")
    val native: Native?,
    @SerializedName("rewarded")
    val rewarded: Rewarded?,
)

data class Banner(
    @SerializedName("adUnitId")
    val adUnitId: String?, // ca-app-pub-3940256099942544/6300978111
    @SerializedName("isEnable")
    val isEnable: Boolean? // true
)

data class Interstitial(
    @SerializedName("adUnitId")
    val adUnitId: String?, // ca-app-pub-3940256099942544/1033173712
    @SerializedName("isEnable")
    val isEnable: Boolean? // true
)

data class Native(
    @SerializedName("adUnitId")
    val adUnitId: String?, // ca-app-pub-3940256099942544/2247696110
    @SerializedName("isEnable")
    val isEnable: Boolean? // true
)

data class Rewarded(
    @SerializedName("adUnitId")
    val adUnitId: String?, // ca-app-pub-3940256099942544/5224354917
    @SerializedName("isEnable")
    val isEnable: Boolean? // true
)