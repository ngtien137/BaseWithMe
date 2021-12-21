package com.base.baselibrary.model

import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

data class FileData(
    val file: File, val mediaUri: Uri,
    val mimeType: String = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(file.extension) ?: "",
    val displayName: String = file.nameWithoutExtension,
    val size: Long = file.length()
) {
}