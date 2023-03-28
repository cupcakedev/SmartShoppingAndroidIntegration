package com.smartshopping.smartshoppingandroid

import android.content.Context
import com.smartshopping.smartshoppingandroid.models.*

interface SmartShoppingProvider {
    fun getContext(): Context
    /**
     * Called when a checkout state is received.
     *
     * @param checkoutState The checkout state.
     */
    fun didReceiveCheckoutState(checkoutState: EngineCheckoutState)
    /**
     * Called when an engine configuration is received.
     *
     * @param engineConfig The engine configuration.
     */
    fun didReceiveConfig(engineConfig: EngineConfig)
    /**
     * Called when a final cost has been calculated.
     *
     * @param finalCost The final cost.
     */
    fun didReceiveFinalCost(finalCost: EngineFinalCost)
    /**
     * Called when promo codes have been detected.
     *
     * @param promoCodes The promo codes.
     */
    fun didReceivePromocodes(promoCodes: Array<String>)
    /**
     * Called when there is a change in the engine's progress.
     *
     * @param value The progress value.
     * @param progress The engine's progress state.
     */
    fun didReceiveProgress(value: ProgressStatus, progress: EngineState)

    /**
     * Called when the engine detects a new promo code.
     *
     * @param currentCode The current promo code.
     */
    fun didReceiveCurrentCode(currentCode: String)
    /**
     * Called when the engine detects the best promo code.
     *
     * @param bestCode The best promo code.
     */
    fun didReceiveBestCode(bestCode: String)
    /**
     * Called when the engine detects a state change in the detection process.
     *
     * @param detectState The detection state.
     */
    fun didReceiveDetectState(detectState: EngineDetectState)
    /**
     * Called when the engine detects a change in the checkout process.
     *
     * @param value The checkout value.
     * @param engineState The engine's state.
     */
    fun didReceiveCheckout(value: Boolean, engineState: EngineState)
}