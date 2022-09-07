package com.souzaemerson.marvelproject.core.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson

class PreferencesUtil(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)

    fun <T> putObjectIntoPreferences(key: String, obj: T) {
        convertObjectToJson(obj).let {
            preferencesEditor().putString(key, it).apply()
        }
    }

    fun <T> getObjectFromPreferences(key: String, objectClass: Class<T>): T? {
        val jsonObject = sharedPreferences.getString(key, null)
        return jsonObject?.let { convertJsonToObject(it, objectClass) }
    }

    fun containsObjectInPreferences(key: String): Boolean = sharedPreferences.contains(key)

    private fun preferencesEditor() = sharedPreferences.edit()

    private fun <T> convertObjectToJson(obj: T): String {
        Gson().let { gson ->
            return gson.toJson(obj)
        }
    }

    private fun <T> convertJsonToObject(jsonObject: String, clazz: Class<T>): T {
        Gson().let { gson ->
            return gson.fromJson(jsonObject, clazz)
        }
    }

    companion object {
        private const val PREFERENCES = "preferences"
    }
}