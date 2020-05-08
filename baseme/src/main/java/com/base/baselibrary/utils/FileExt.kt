package com.base.baselibrary.utils

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