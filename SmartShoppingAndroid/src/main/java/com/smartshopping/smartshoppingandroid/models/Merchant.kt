package com.smartshopping.smartshoppingandroid.models

import com.google.gson.annotations.SerializedName

data class Merchant(
    @SerializedName("shopName") var shopName: String? = null,
    @SerializedName("shopId") var shopId: String? = null,
    @SerializedName("shopUrl") var shopUrl: String? = null,
    @SerializedName("checkoutUrl") var checkoutUrl: String? = null
)