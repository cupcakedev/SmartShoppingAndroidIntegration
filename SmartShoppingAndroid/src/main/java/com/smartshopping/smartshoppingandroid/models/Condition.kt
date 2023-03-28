package com.smartshopping.smartshoppingandroid.models

import com.google.gson.annotations.SerializedName

data class Condition (
    @SerializedName("type") val type: String,
    @SerializedName("value") val value: Selector?,
    @SerializedName("operands") val operands: List<Condition>?
)