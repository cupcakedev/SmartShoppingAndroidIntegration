package com.smartshopping.smartshoppingandroid.models

import com.google.gson.annotations.SerializedName

data class Command(
    @SerializedName("type") val type: String,
    @SerializedName("condition") val condition: Condition?,
    @SerializedName("do") val `do`: List<Command>?,
    @SerializedName("else") val `else`: List<Command>?,
    @SerializedName("response") val response: List<InteractResponse>?,
    @SerializedName("timeout") val timeout: Int?,
    @SerializedName("selector") val selector: Selector?,
    @SerializedName("value") val value: String?,
    @SerializedName("action") val action: InteractAction?,
    @SerializedName("cut") val cut: ExtractCut?,
    @SerializedName("target") val target: String?,
    @SerializedName("level") val level: Int?,
    @SerializedName("format") val format: String?,
    @SerializedName("codeIsValid") val codeIsValid: Boolean?,
    @SerializedName("anchor") val anchor: String?,
    @SerializedName("place") val place: String?
)

data class InteractResponse(
    @SerializedName("type") val type: String,
    @SerializedName("selector") val selector: Selector
)

data class InteractAction(
    @SerializedName("type") val type: String,
    @SerializedName("selector") val selector: Selector
)

data class ExtractCut(
    @SerializedName("position") val position: String,
    @SerializedName("leftEdge") val leftEdge: String?,
    @SerializedName("rightEdge") val rightEdge: String?
)
