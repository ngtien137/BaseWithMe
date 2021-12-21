package com.base.baselibrary.utils.share_preference

import android.app.Application

val LongClass = Long::class.java
val IntClass = Int::class.java
val StringClass = String::class.java
val FloatClass = Float::class.java
val BooleanClass = Boolean::class.java

object SharePreferencesUtils {

    var basePreference: BasePreference? = null

    fun Application.initPrefData(preferenceName: String) {
        basePreference = BasePreference(preferenceName, this)
    }

    inline fun <reified T> getSyncPrefData(key: String): T = basePreference!!.get(key)
    inline fun <reified T> getSyncPrefData(key: String, defaultValue: T): T =
        basePreference!!.get(key, defaultValue)

    fun <T> putSyncPrefData(key: String, value: T) = basePreference!!.put(key, value)
    
}