package com.base.baselibrary.utils

import android.graphics.Bitmap
import java.io.FileOutputStream
import java.io.IOException


object BitmapUtils {

    fun Bitmap.saveToFile(
        fileName: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Boolean {
        try {
            FileOutputStream(fileName).use { out ->
                this.compress(format, 100, out) // bmp is your Bitmap instance
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}