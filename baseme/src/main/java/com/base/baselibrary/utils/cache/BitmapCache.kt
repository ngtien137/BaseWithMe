package com.base.baselibrary.utils.cache

import android.graphics.Bitmap
import android.util.LruCache
import java.lang.Exception

object BitmapCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

    private val cacheSize = maxMemory / 8

    private val mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            return (value?.byteCount ?: 0) / 1024
        }
    }

    fun addBitmap(key: String?, bitmap: Bitmap?) {
        if (getBitmap(key) == null) {
            mMemoryCache.put(key, bitmap)
        }
    }

    fun clearBitmap(key: String?) {
        if (getBitmap(key) != null) {
            try {
                getBitmap(key)?.recycle()
            } catch (e: Exception) {

            }
            mMemoryCache.remove(key)
        }
    }

    fun getBitmap(key: String?): Bitmap? {
        return mMemoryCache[key]
    }

}



