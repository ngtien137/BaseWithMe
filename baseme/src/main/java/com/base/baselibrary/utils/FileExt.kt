package com.base.baselibrary.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun String.isFileExists(
    onExists: () -> Unit,
    onNotExists: () -> Unit = {

    }
) {
    if (File(this).exists()) {
        onExists()
    } else {
        onNotExists()
    }
}

fun String.getFolder(): String {
    val dir = File(this)
    if (!dir.exists() || !dir.isDirectory)
        dir.mkdirs()
    return if (this.endsWith("/"))
        this
    else
        "$this/"
}

fun Context.getRealPathFromURI(uri:Uri):String {
    var path = ""
    if (contentResolver != null) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(idx)
            cursor.close()
        }
    }
    return path
}