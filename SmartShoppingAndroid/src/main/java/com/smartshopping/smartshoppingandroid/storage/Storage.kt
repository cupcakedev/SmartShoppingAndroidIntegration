package com.smartshopping.smartshoppingandroid.storage

import android.content.Context
import android.util.Log
import com.smartshopping.smartshoppingandroid.filesmanager.FilesManager
import com.smartshopping.smartshoppingandroid.models.*
import com.google.gson.Gson

data class Storage(
    val context: Context,
    val cacheFilename: String = "coreStorage.cache",
    private var isAlreadyRead: Boolean = false,
    // An array of `Merchant` objects representing shops.
    var shops: List<Merchant> = emptyList(),
    // An array of `EngineConfig` objects representing default configurations.
    var defaultConfigs: List<EngineConfig> = emptyList(),
    // A dictionary containing default selectors for each selector key.
    var defaultSelectors: Map<String, List<Selector>> = emptyMap(),
    // An object representing persisted engine state.
    var persisted: EnginePersistedState? = null,
    // A private dictionary for holding cached engine configurations.
    private val _cachedConfigs: MutableMap<String, CachedEngineConfig> = mutableMapOf()
) {
    // A computed property for getting and setting the cached engine configurations.
    var cachedConfigs: Map<String, CachedEngineConfig>
        get() = _cachedConfigs.toMap()
        set(value) {
            _cachedConfigs.clear()
            _cachedConfigs.putAll(value)
            saveToDisk()
        }

    /**
     * Adds a cached engine configuration to the `_cachedConfigs` dictionary.
     *
     * @param id The ID of the cached engine configuration.
     * @param config The cached engine configuration to add.
     */
    fun addCachedConfig(id: String, config: CachedEngineConfig) {
        _cachedConfigs[id] = config
        saveToDisk()
    }

    /**
     * Saves the cached engine configurations to disk.
     */
    private fun saveToDisk() {
        try {
            val gson = Gson()
            val jsonData = gson.toJson(_cachedConfigs)
            FilesManager.save(context, cacheFilename, jsonData)
        } catch (e: Exception) {
            Log.e("Storage", "Error saving cache to disk: ${e.message}")
        }
    }
}