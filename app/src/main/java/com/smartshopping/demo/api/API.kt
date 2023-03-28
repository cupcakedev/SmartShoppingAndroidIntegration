package com.smartshopping.demo.api

import com.smartshopping.demo.models.Shop
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class API {
    companion object{
        var shops: List<Shop> = emptyList()
        var cachedPromoodes: MutableMap<String, List<String>> = mutableMapOf()

        suspend fun requireShops() {
            return try {
                val type = object : TypeToken<List<Shop>>() {}.type
                val url = "https://api2.smartshopping.ai/demo/shops"
                val (data, _) = fetch(url)
                val gson = Gson()
                shops = gson.fromJson(String(data), type)
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }


        suspend fun requirePromocodes(nUrl: String): List<String> {
            try {
                if (shops.isEmpty()) {
                    return emptyList()
                }
                val locatedShop = shops.firstOrNull {
                    it.urlPattern.toRegex().containsMatchIn(nUrl)
                }
                if (locatedShop == null) {
                    return emptyList()
                }
                cachedPromoodes[locatedShop.id]?.let { cachedCodes ->
                    return cachedCodes
                }
                val type = object : TypeToken<List<String>>() {}.type
                val url = "https://api2.smartshopping.ai/demo/${locatedShop.id}"
                val (data, _) = fetch(url)
                val gson = Gson()
                val promocodes: List<String> = gson.fromJson(String(data), type)
                cachedPromoodes[locatedShop.id] = promocodes
                return promocodes
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }

        private suspend fun fetch(
            url: String,
            method: String = "GET",
            body: ByteArray? = null,
        ): Pair<ByteArray, Any> {
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
            val inputStream = connection.inputStream
            return inputStream.readBytes() to connection.responseMessage
        }
    }
}
