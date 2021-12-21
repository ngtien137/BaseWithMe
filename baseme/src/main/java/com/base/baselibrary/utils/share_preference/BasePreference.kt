package com.base.baselibrary.utils.share_preference

import  android.app.Application
import android.content.SharedPreferences

class BasePreference(val PREF_NAME: String, val application: Application) {
    val sharePref by lazy {
        application.getSharedPreferences(PREF_NAME, Application.MODE_PRIVATE)
    }

    fun getInstance() = sharePref

    inline fun <reified T> get(key: String): T {
        return when (T::class.java) {
            String::class.javaObjectType -> {
                sharePref.getString(key, "") as T
            }
            Int::class.javaObjectType -> {
                sharePref.getInt(key, -1) as T
            }
            Float::class.javaObjectType -> {
                sharePref.getFloat(key, -1f) as T
            }
            Long::class.javaObjectType -> {
                sharePref.getLong(key, -1L) as T
            }
            Boolean::class.javaObjectType -> {
                sharePref.getBoolean(key, false) as T
            }
            else -> T::class.java.newInstance()
        }
    }

    inline fun <reified T> get(key: String, defaultValue: T): T {
        return when (T::class.java) {
            String::class.javaObjectType -> {
                sharePref.getString(key, defaultValue as String) as T
            }
            Int::class.javaObjectType -> {
                sharePref.getInt(key, defaultValue as Int) as T
            }
            Float::class.javaObjectType -> {
                sharePref.getFloat(key, defaultValue as Float) as T
            }
            Long::class.javaObjectType -> {
                sharePref.getLong(key, defaultValue as Long) as T
            }
            Boolean::class.javaObjectType -> {
                sharePref.getBoolean(key, defaultValue as Boolean) as T
            }
            else -> T::class.java.newInstance()
        }
    }

    fun <T> put(key: String, data: T) {
        val editor: SharedPreferences.Editor = sharePref.edit()
        when (data) {
            is String -> {
                editor.putString(key, data as String)
            }
            is Boolean -> {
                editor.putBoolean(key, (data as Boolean))
            }
            is Float -> {
                editor.putFloat(key, (data as Float))
            }
            is Int -> {
                editor.putInt(key, data)
            }
            is Long -> {
                editor.putLong(key, (data as Long))
            }
        }
        editor.apply()
    }
}