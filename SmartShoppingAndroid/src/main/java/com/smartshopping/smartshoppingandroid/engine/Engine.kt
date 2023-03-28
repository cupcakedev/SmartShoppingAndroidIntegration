package com.smartshopping.smartshoppingandroid.engine

import android.content.Context
import com.smartshopping.smartshoppingandroid.SmartShoppingProvider
import com.smartshopping.smartshoppingandroid.constants.Constants
import com.smartshopping.smartshoppingandroid.engine.utils.FetchUtils
import com.smartshopping.smartshoppingandroid.models.*
import com.smartshopping.smartshoppingandroid.storage.Storage
import com.google.gson.Gson

class Engine(
    private val clientID: String,
    private val key: String,
    private val promoCodes: Array<String>,
    private val context: Context,
    private var provider: SmartShoppingProvider,
    private val sendMessage: (Any) -> Unit,
    private val evaluateJavaScript: (String) -> Unit,
) {
    private val fetchUtils = FetchUtils(clientID, key)
    private val storage = Storage(context)
    suspend fun install() {
        storage.shops = fetchUtils.requireShops()
        storage.defaultConfigs = fetchUtils.requireDefaultConfig()
        prepareDefaultSelectors()
    }

    suspend fun startEngine(url: String, codes: Array<String>) {
        val locatedConfig = locateConfig(url)
        if (locatedConfig == null) {
            checkPage()
            return
        }
        val isCheckoutPage = isCheckout(url, locatedConfig)
        initFlow(locatedConfig, isCheckoutPage, codes)
    }

    private fun checkPage() {
        val checkMessage = Message.CheckMessage(
            type = "smartshopping_check",
            defaultSelectors = storage.defaultSelectors
        )
        sendMessage(checkMessage)
    }

    private fun initFlow(
        config: EngineConfig,
        isCheckoutPage: Boolean,
        codes: Array<String>
    ) {
        try {
            val gson = Gson()
            val jsonConfigData = gson.toJson(config)
            val initMessage = Message.InitMessage(
                type = "smartshopping_init",
                config = jsonConfigData,
                checkout = isCheckoutPage,
                promocodes = codes,
                persistedState = storage.persisted
            )
            sendMessage(initMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isCheckout(url: String, config: EngineConfig): Boolean {
        return url.contains(config.checkoutUrl)
    }

    fun initEngine() {
        val js = """
              if (window && !window.smartShoppingEngine) {
                  window.smartShoppingEngine = new SmartShopping.Engine();
                  window.sendMessageToNative = function sendMessageToNative(event, message) {
                      window["${Constants.messageBridgeKey}"].onMessageReceived(JSON.stringify({event, message}));
                  };
        
                  smartShoppingEngine.subscribe({
                    config: (value) => sendMessageToNative("config", value),
                    checkoutState: (value) => sendMessageToNative("checkoutState",value),
                    finalCost: (value) => sendMessageToNative("finalCost",value),
                    promocodes: (value) => sendMessageToNative("promocodes",value),
                    progress: (value, state) => sendMessageToNative("progress", {value, state}),
                    currentCode: (value) => sendMessageToNative("currentCode", value),
                    bestCode: (value) => sendMessageToNative("bestCode", value),
                    detectState: (value) => sendMessageToNative("detectState", value),
                    checkout: (value, state) => sendMessageToNative("checkout", {value, state})
                  });
             }
        """
        evaluateJavaScript(js)
    }

    fun sendCodes(codes: Array<String>) {
        val sendMessage = Message.SendCodeMessage(
            type = "smartshopping_codes", promocodes = codes
        )
        sendMessage(sendMessage)
    }

    fun inspect() {
        val js = """
                if (document.readyState === 'complete') {
                    smartShoppingEngine.inspect();
                } else {
                    const inspector = () => {
                        smartShoppingEngine.inspect();
                        document.removeEventListener('load', inspector);
                    };
                    document.addEventListener('load', inspector);
                }
        """
        evaluateJavaScript(js)
    }

    fun detect() {
        val js = """
               if (document.readyState === 'complete') {
                   smartShoppingEngine.detect();
               } else {
                   const detector = () => {
                       smartShoppingEngine.detect();
                       document.removeEventListener('load', inspector);
                   };
                   document.addEventListener('load', inspector);
               }
       """
        evaluateJavaScript(js)
    }

    fun notifyAboutShowModal() {
        val js = "smartShoppingEngine.notifyAboutShowModal()"
        evaluateJavaScript(js)
    }


    fun notifyAboutCloseModal() {
        val js = "smartShoppingEngine.notifyAboutCloseModal()"
        evaluateJavaScript(js)
    }


    fun apply() {
        val js = "smartShoppingEngine.apply()"
        evaluateJavaScript(js)
    }


    fun applyBest() {
        val js = "smartShoppingEngine.applyBest()"
        evaluateJavaScript(js)
    }


    fun fullCycle() {
        val js = "smartShoppingEngine.fullCycle()"
        evaluateJavaScript(js)
    }

    fun abort() {
        val js = "smartShoppingEngine.abort()"
        evaluateJavaScript(js)
    }

    /**
     * Initializes the engine with the default configuration for the given `configId`.
     *
     * @param configId The ID of the configuration to be used for initialization.
     */
    private fun defaultConfigInit(configId: String) {
        val config = storage.defaultConfigs.firstOrNull { it.shopId == configId } ?: return
        val gson = Gson()
        val jsonConfigData = gson.toJson(config)
        val initMessage = Message.InitMessage(
            type = "smartshopping_init",
            config = jsonConfigData,
            checkout = true,
            promocodes = promoCodes,
            persistedState = null
        )
        sendMessage(initMessage)
    }



    /**
     * Locates a Merchant for a given URL.
     *
     * @param url The URL to locate a Merchant for.
     *
     * @return The located Merchant, if found.
     */
    fun locateShop(url: String): Merchant? {
        val locatedShop = storage.shops.firstOrNull { merchant ->
            Regex(merchant.shopUrl ?: "").containsMatchIn(url)
        }
        return locatedShop
    }


    /**
     * Loads an EngineConfig from cache or the server.
     *
     * @param shopId The ID of the shop to load the EngineConfig for.
     *
     * @return The loaded EngineConfig, or null if it could not be loaded.
     */
    suspend fun loadConfig(shopId: String): EngineConfig? {
        val cacheRequest = storage.cachedConfigs
        val targetConfig = cacheRequest[shopId]
        return if (targetConfig != null && (targetConfig.timestamp) > 6 * 3600 * 1000) {
            targetConfig.config
        } else {
            val response = fetchUtils.requireShopConfig(shopId)
            response?.let {
                val cachedShopConfig = CachedEngineConfig(
                    timestamp = System.currentTimeMillis(),
                    config = it
                )
                storage.addCachedConfig(id = shopId, config = cachedShopConfig)
            }
            response
        }
    }



    /**
     * Locates the EngineConfig for a given URL.
     *
     * @param url The URL to locate the EngineConfig for.
     *
     * @return The located EngineConfig, if found.
     */
    suspend fun locateConfig(url: String): EngineConfig? {
        val locatedShop = locateShop(url)
        if (locatedShop == null) {
            return null
        }
        val locatedConfig = loadConfig(locatedShop.shopId!!)
        return locatedConfig
    }

    /**
     * Prepares default selectors for later use.
     */
    fun prepareDefaultSelectors() {
        val newObjects = mutableMapOf<String, List<Selector>>()
        storage.defaultConfigs.forEach { config ->
            newObjects[config.shopId] = config.selectorsToCheck
        }
        storage.defaultSelectors = newObjects
    }

    fun handleMessage(message: Message) {
        when (message) {
            is Message.EngineConfigMessage -> provider.didReceiveConfig(message.config)
            is Message.BestCode -> provider.didReceiveBestCode(message.code)
            is Message.CheckMessage -> {}
            is Message.CheckoutMessage -> provider.didReceiveCheckout(message.value, message.config)
            is Message.ClearPersistMessage -> storage.persisted = null
            is Message.ConfigMessage -> defaultConfigInit(message.config)
            is Message.CurrentCode -> provider.didReceiveCurrentCode(message.code)
            is Message.EngineCheckoutStateMessage -> provider.didReceiveCheckoutState(message.state)
            is Message.EngineDetectStateMessage -> provider.didReceiveDetectState(message.engineDetectState)
            is Message.EngineFinalCostMessage -> provider.didReceiveFinalCost(message.finalCost)
            is Message.InitMessage -> {}
            is Message.LogMessage -> {}
            is Message.PersistMessage -> storage.persisted = message.persistedState
            is Message.ProgressMessage -> provider.didReceiveProgress(message.value, message.config)
            is Message.PromoCodesMessage -> provider.didReceivePromocodes(message.promocodes)
            is Message.SendCodeMessage -> {}
            else -> {}
        }
    }
}