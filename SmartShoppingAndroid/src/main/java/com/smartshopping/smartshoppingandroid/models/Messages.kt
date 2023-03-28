package com.smartshopping.smartshoppingandroid.models

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

//data class CheckMessage(
//    @SerializedName("type") val type: String,
//    @SerializedName("defaultSelectors") val defaultSelectors: Map<String, List<Selector>>
//)
//
//data class InitMessage(
//    @SerializedName("type") val type: String,
//    @SerializedName("config") val config: String?,
//    @SerializedName("checkout") val checkout: Boolean,
//    @SerializedName("promocodes") val promocodes: List<String>?,
//    @SerializedName("persistedState") val persistedState: EnginePersistedState?
//)

data class CheckoutMessage(
    @SerializedName("value") val value: Boolean,
    @SerializedName("state") val config: EngineState
)

enum class ProgressStatus(@SerializedName("value") val rawValue: String) {
    @SerializedName("INSPECT_END")
    INSPECT_END("INSPECT_END"),

    @SerializedName("AWAIT")
    AWAIT("AWAIT"),

    @SerializedName("INACTIVE")
    INACTIVE("INACTIVE"),

    @SerializedName("APPLY")
    APPLY("APPLY"),

    @SerializedName("APPLY_END")
    APPLY_END("APPLY_END"),

    @SerializedName("APPLY-BEST")
    APPLY_BEST("APPLY-BEST"),

    @SerializedName("APPLY-BEST_END")
    APPLY_BEST_END("APPLY-BEST_END"),

    @SerializedName("FAIL")
    FAIL("FAIL"),

    @SerializedName("SUCCESS")
    SUCCESS("SUCCESS"),

    @SerializedName("STARTED")
    STARTED("STARTED"),

    @SerializedName("DETECT")
    DETECT("DETECT"),

    @SerializedName("DETECT_END")
    DETECT_END("DETECT_END"),

    @SerializedName("COUPON-EXTRACTED")
    COUPON_EXTRACTED("COUPON-EXTRACTED"),

    @SerializedName("CANCEL")
    CANCEL("CANCEL"),

    @SerializedName("ERROR")
    ERROR("ERROR"),

    @SerializedName("INSPECT")
    INSPECT("INSPECT")
}

data class LogEvent(
    @SerializedName("type")
    val type: String,
    @SerializedName("shop")
    val shop: String,
    @SerializedName("total")
    val total: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("valid")
    val valid: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("codes")
    val codes: String?,
    @SerializedName("layoutPage")
    val layoutPage: String?
)



sealed class Message {
    class CheckMessage(
        @SerializedName("type") val type: String,
        @SerializedName("defaultSelectors") val defaultSelectors: Map<String, List<Selector>>
    ) : Message()

    class SendCodeMessage(
        @SerializedName("type") val type: String,
        @SerializedName("promocodes") val promocodes: Array<String>?,
    ) : Message()
    class InitMessage(
        @SerializedName("type") val type: String,
        @SerializedName("config") val config: String?,
        @SerializedName("checkout") val checkout: Boolean,
        @SerializedName("promocodes") val promocodes: Array<String>?,
        @SerializedName("persistedState") val persistedState: EnginePersistedState?
    ) : Message()

    class PersistMessage(
        @SerializedName("persistedState") val persistedState: EnginePersistedState
    ) : Message()

    class ClearPersistMessage(
        @SerializedName("type") val type: String,
    ) : Message()
    class ConfigMessage(val config: String) : Message()
    class EngineConfigMessage(val config: EngineConfig) : Message()
    class EngineCheckoutStateMessage(val state: EngineCheckoutState) : Message()
    class EngineFinalCostMessage(val finalCost: EngineFinalCost) : Message()
    class PromoCodesMessage(val promocodes: Array<String>) : Message()
    class EngineDetectStateMessage(val engineDetectState: EngineDetectState) : Message()
    class CheckoutMessage(
        @SerializedName("value") val value: Boolean,
        @SerializedName("state") val config: EngineState
    ) : Message()
    class ProgressMessage(
        @SerializedName("value") val value: ProgressStatus,
        @SerializedName("state") val config: EngineState
    ) : Message()
    class CurrentCode(val code: String) : Message()
    class BestCode(val code: String) : Message()
    class LogMessage(
        @SerializedName("type") val type: String,
        @SerializedName("event") val event: LogEvent,
    ) : Message()
    class NullMessage() : Message()

    companion object {
        @Throws(ParseError::class)
        fun fromJson(json: String): Message {
            val jsonElement = JsonParser.parseString(json)
            if (jsonElement.isJsonPrimitive) {
                return ConfigMessage(jsonElement.asString)
            }
            if (jsonElement.isJsonNull) {
                return NullMessage();
            }
            val jsonObj = jsonElement.asJsonObject
            val type = jsonObj.get("type")?.asString
            val event = jsonObj.get("event")?.asString
            val message = jsonObj.get("message")


            return when (type) {
                "smartshopping_persist" -> Gson().fromJson(json, PersistMessage::class.java)
                "smartshopping_clear" -> Gson().fromJson(
                    json,
                    ClearPersistMessage::class.java
                )
                null -> when (event) {
                    null -> {
                        val string = Gson().fromJson(json, String::class.java)
                        ConfigMessage(string)
                    }
                    "config" -> {
                        val engineConfig = Gson().fromJson(message, EngineConfig::class.java).let {
                            EngineConfigMessage(it)
                        }
                        engineConfig
                    }
                    "checkoutState" -> {
                        val engineCheckoutState =
                            Gson().fromJson(message, EngineCheckoutState::class.java).let {
                                EngineCheckoutStateMessage(it)
                            }
                        engineCheckoutState
                    }
                    "finalCost" -> {
                        val type = object : TypeToken<EngineFinalCost>() {}.type
                        val engineFinalCost: EngineFinalCost = Gson().fromJson(message, type)
                        EngineFinalCostMessage(engineFinalCost)
                    }
                    "promocodes" -> {
                        val type = object : TypeToken<Array<String>>() {}.type
                        val promoCodes: Array<String> = Gson().fromJson(message, type)
                        PromoCodesMessage(promoCodes)
                    }
                    "detectState" -> {
                        val engineDetectState = Gson().fromJson(message, EngineDetectState::class.java).let {
                            EngineDetectStateMessage(it)
                        }
                        engineDetectState
                    }
                    "checkout" -> {
                        val checkoutMessage = Gson().fromJson(message, CheckoutMessage::class.java)
                        checkoutMessage
                    }
                    "progress" -> {
                        val progressMessage = Gson().fromJson(message, ProgressMessage::class.java)
                        progressMessage
                    }
                    "currentCode" -> {
                        val string = Gson().fromJson(message, String::class.java)
                        CurrentCode(string)
                    }
                    "bestCode" -> {
                        val string = Gson().fromJson(message, String::class.java)
                        BestCode(string)
                    }
                    else -> throw ParseError("Invalid event type: $event")
                }
                "smartshopping_log" -> {
                    val logMessage = Gson().fromJson(json, LogMessage::class.java)
                    logMessage
                }
                "smartshopping_codes" -> {
                    val sendCodeMessage = Gson().fromJson(json, SendCodeMessage::class.java)
                    sendCodeMessage
                }
                else -> throw ParseError("Invalid message type: $type")
            }
        }

        fun isString(string: String) {

        }
    }


    class ParseError(message: String) : Exception(message)
}
