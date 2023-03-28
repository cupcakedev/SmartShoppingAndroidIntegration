package com.smartshopping.demo.models

import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("id") var id: String,
    @SerializedName("urlPattern") var urlPattern: String,
)