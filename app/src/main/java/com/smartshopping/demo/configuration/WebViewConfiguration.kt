package com.smartshopping.demo.configuration

import android.graphics.Bitmap
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
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
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
            webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36"
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.stockx.com")
            webView.settings.databaseEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.allowFileAccess = true
            webView.settings.allowFileAccessFromFileURLs = true
            webView.settings.allowContentAccess = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
            webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            webView.settings.textZoom = 100
            webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            webView.setLayerType(WebView.LAYER_TYPE_NONE, null)
            webView.settings.setSupportMultipleWindows(false)

            WebView.setWebContentsDebuggingEnabled(true)
            smartShopping.install(webView)
        }
    }
}