package com.smartshopping.smartshoppingandroid

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.smartshopping.smartshoppingandroid.constants.Constants
import com.smartshopping.smartshoppingandroid.engine.Engine
import com.smartshopping.smartshoppingandroid.models.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class SmartShopping(clientID: String, key: String, provider: SmartShoppingProvider) {
    private lateinit var webView: WebView
    public val engine = Engine(
        clientID = clientID,
        key = key,
        context = provider.getContext(),
        promoCodes = emptyArray(),
        provider = provider,
        sendMessage = { message -> this.sendMessageToWebView(message) }
    ) { script -> this.executeJavaScript(script) }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                engine.install()
            }
        }
    }

    fun install(webView: WebView) {
        this.webView = webView
        setMessageBridge()
    }

    suspend fun startEngine(url: String, codes: Array<String>) {
        loadJS()
        engine.initEngine()
        engine.startEngine(url, codes)
    }

    private fun executeJavaScript(js: String) {
        webView.post {
            webView.evaluateJavascript(js) {
            }
        }
    }

    private fun sendMessageToWebView(message: Any) {
        val gson = Gson()
        val jsonMessage = gson.toJson(message)
        val event = Constants.messageBridgeEvent
        executeJavaScript(
            """
                window.dispatchEvent(new CustomEvent("$event", {
                    detail: $jsonMessage
                  }));
             """
        )
    }

    private fun loadJS() {
        val js = this::class.java.classLoader.getResourceAsStream("index.js")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw RuntimeException("File not found: index.js")
        executeJavaScript(js)
    }

    fun setPromoCodes(promoCodes: Array<String>) {
        engine.sendCodes(promoCodes)
    }

    private fun setMessageBridge() {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun onMessageReceived(message: String) {
                val parsedMessage = Message.fromJson(message)
                engine.handleMessage(parsedMessage)
            }
        }, Constants.messageBridgeKey)
    }
}
