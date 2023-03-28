package com.smartshopping.smartshoppingandroid.models

import com.google.gson.annotations.SerializedName

typealias Selector = Any

data class ShadowSelector(
    @SerializedName("shadowRoots") val shadowRoots: List<String>,
    @SerializedName("target") val target: String
)
