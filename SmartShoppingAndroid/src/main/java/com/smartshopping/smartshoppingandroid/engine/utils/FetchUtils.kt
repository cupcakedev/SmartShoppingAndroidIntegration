package com.smartshopping.smartshoppingandroid.engine.utils

import com.smartshopping.smartshoppingandroid.constants.Constants
import com.smartshopping.smartshoppingandroid.cryptojs.CryptoJS
import com.smartshopping.smartshoppingandroid.models.EngineConfig
import com.smartshopping.smartshoppingandroid.models.Merchant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FetchUtils (
    private val clientID: String,
    private val key: String,
) {
    suspend fun requireShops(): List<Merchant> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "${Constants.serverUrl}/shop/urls?clientID=$clientID"
                val (data, _) = fetch(url)
                val gson = Gson()
                val listType = object : TypeToken<List<Merchant>>() {}.type
                var shops: List<Merchant> = gson.fromJson(String(data), listType)
                shops
            } catch (error: Exception) {
                error.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun requireDefaultConfig(): List<EngineConfig> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "${Constants.serverUrl}/shop/defaultConfigs?clientID=$clientID"
                val (data, _) = fetch(url)
                val gson = Gson()
                val listType = object : TypeToken<List<EngineConfig>>() {}.type
                var defaultConfig: List<EngineConfig> = gson.fromJson(String(data), listType)
                defaultConfig
            } catch (error: Exception) {
                error.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun requireShopConfig(shopId: String): EngineConfig? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "${Constants.serverUrl}/shop/$shopId?clientID=$clientID"
                val (data, _) = fetch(url)
                val gson = Gson()
                var defaultConfig = gson.fromJson(String(data), EngineConfig::class.java)
                defaultConfig
            } catch (error: Exception) {
                error.printStackTrace()
                null
            }
        }
    }

    private suspend fun fetch(url: String, method: String = "GET", body: ByteArray? = null, isResponseEncrypted: Boolean = true): Pair<ByteArray, Any> {
        val urlObj = URL(url)
        val connection = urlObj.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.doInput = true
        connection.doOutput = body != null
        if (body != null) {
            connection.setRequestProperty("Content-Type", "application/json")
            connection.outputStream.use { os ->
                os.write(body)
            }
        }
        val statusCode = connection.responseCode
        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw IOException("HTTP error code: $statusCode")
        }
        val inputStream = if (isResponseEncrypted) {
            val jsonString = connection.inputStream.bufferedReader().readText()
            val decrypted = decrypt(jsonString)
            if (decrypted != null) {
                ByteArrayInputStream((decrypted).toByteArray(Charsets.UTF_8))
            } else {
                ByteArrayInputStream("".toByteArray(Charsets.UTF_8))
            }
        } else {
            connection.inputStream
        }
        return inputStream.readBytes() to connection.responseMessage
    }

    private fun decrypt(message: String): String? {
        return CryptoJS.decrypt(message, key)
    }


}