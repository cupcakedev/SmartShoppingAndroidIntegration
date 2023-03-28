package com.smartshopping.smartshoppingandroid.filesmanager
import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


object FilesManager {
    fun save(context: Context, filename: String, data: String) {
        val file = File(context.filesDir, filename)
        try {
            FileOutputStream(file).use { stream ->
                stream.write(data.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Read data from a file in the app folder
    fun read(context: Context, filename: String): String {
        val file = File(context.filesDir, filename)
        val stringBuilder = StringBuilder()
        try {
            FileInputStream(file).use { stream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (stream.read(buffer).also { length = it } != -1) {
                    stringBuilder.append(String(buffer, 0, length))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}
