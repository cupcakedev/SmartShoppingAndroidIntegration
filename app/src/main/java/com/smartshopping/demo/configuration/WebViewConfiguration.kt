package com.smartshopping.demo.configuration

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smartshopping.module.SmartShopping
import com.smartshopping.demo.models.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class WebViewConfiguration {
    companion object {
        fun setupWebView(
            webView: WebView,
            smartShopping: SmartShopping,
            viewState: ViewState,
        ) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    viewState.currentUrl.value = url
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Main) {
                            try {
                                if (url != null) {
                                    smartShopping.startEngine(url, emptyArray())
                                }
                            } catch (e: IOException) {
                                // Handle the exception
                            }
                        }
                    }
                }
            }
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.stockx.com")
            WebView.setWebContentsDebuggingEnabled(true)
            smartShopping.install(webView)
        }
    }
}