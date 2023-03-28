package com.smartshopping.smartshoppingandroid.models

import com.google.gson.annotations.SerializedName

data class EngineConfig(
    @SerializedName("version") val version: Double,
    @SerializedName("taskId") val taskId: String,
    @SerializedName("shopId") val shopId: String,
    @SerializedName("shopName") val shopName: String,
    @SerializedName("shopUrl") val shopUrl: String,
    @SerializedName("checkoutUrl") val checkoutUrl: String,
    @SerializedName("extendedLogs") val extendedLogs: Boolean,
    @SerializedName("extendedReports") val extendedReports: Boolean,
    @SerializedName("inspect") val inspect: List<Command>,
    @SerializedName("detect") val detect: List<Command>,
    @SerializedName("apply") val apply: List<Command>,
    @SerializedName("applyBest") val applyBest: List<Command>,
    @SerializedName("selectorsToCheck") val selectorsToCheck: List<Selector>
)

data class CachedEngineConfig(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("config") val config: EngineConfig
)

data class EnginePersistedState(
    @SerializedName("context") val context: EngineExecContext,
    @SerializedName("finalCost") val finalCost: EngineFinalCost,
    @SerializedName("checkoutState") val checkoutState: EngineCheckoutState,
    @SerializedName("promocodes") val promocodes: List<String>
)

data class EngineExecContext(
    @SerializedName("code") val code: String,
    @SerializedName("codeIsValid") val codeIsValid: Boolean,
    @SerializedName("value") val value: String?,
    @SerializedName("returnCode") val returnCode: Int,
    @SerializedName("criticalError") val criticalError: Boolean,
    @SerializedName("anchor") val anchor: String?,
    @SerializedName("anchorCode") val anchorCode: String?,
    @SerializedName("anchorStage") val anchorStage: String?
)

typealias EngineFinalCost = Map<String, Double>

data class EngineCheckoutState(
    @SerializedName("total") val total: Double?
)

data class EngineDetectState(
    @SerializedName("userCode") val userCode: String,
    @SerializedName("isValid") val isValid: Boolean
)

data class EngineState(
    @SerializedName("checkoutState") val checkoutState: EngineCheckoutState,
    @SerializedName("finalCost") val finalCost: EngineFinalCost,
    @SerializedName("progress") val progress: String,
    @SerializedName("config") val config: EngineConfig,
    @SerializedName("promocodes") val promocodes: List<String>,
    @SerializedName("detectState") val detectState: EngineDetectState,
    @SerializedName("bestCode") val bestCode: String,
    @SerializedName("currentCode") val currentCode: String,
    @SerializedName("checkout") val checkout: Boolean
)
