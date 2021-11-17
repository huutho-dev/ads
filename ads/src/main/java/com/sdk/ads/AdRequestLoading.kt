package com.sdk.ads

data class AdRequestLoading(
    var adObject: Any? = null,
    var isLoading: Boolean = false,
    var screenLabel: String,
    var type: Constants.AdType
)