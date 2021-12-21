package com.base.baselibrary.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.base.baselibrary.model.FileData
import java.io.*
import java.util.*


object BaseFileUtils {
    fun checkFileExist(context: Context, uri: Uri): Boolean {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            true
        } catch (e: Exception) {
            false
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
            }
        }
    }

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

    fun String.getFolder(): File {
        val dir = File(this)
        if (!dir.exists() || !dir.isDirectory)
            dir.mkdirs()
        return dir
    }

    fun Context.getRealPathFromURI(uri: Uri): String {
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

    /**
     * publicaFolder root is something like
     * [Environment.DIRECTORY_DOCUMENTS]
     * [Environment.DIRECTORY_MUSIC]
     * [Environment.DIRECTORY_MOVIES]
     * [Environment.DIRECTORY_DOWNLOADS]
     * etc...
     */

    fun getSupportedDirectory(folderName: String, publicFolderRoot: String): File? {
        var dir: File?
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                getApplication().getExternalFilesDir(publicFolderRoot)
                    .toString() + "/" + folderName
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/" + folderName)
        }

        if (!dir.exists()) {
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

    //root Uri: MediaStore.Downloads.EXTERNAL_CONTENT_URI, ....
    fun copyFileToDownloads(
        downloadedFile: FileData,
        publicFolder: String,
        onFinish: (path: String) -> Unit = {}
    ): Uri? {
        val context = getApplication()
        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, downloadedFile.displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, downloadedFile.mimeType)
                put(MediaStore.MediaColumns.SIZE, downloadedFile.size)
            }
            resolver.insert(downloadedFile.mediaUri, contentValues)
        } else {
            val authority = "${context.packageName}.provider"
            val destinyFile = File(publicFolder, downloadedFile.file.name)
            val folder = Environment.getExternalStoragePublicDirectory(publicFolder)
            if (!folder.exists())
                folder.mkdirs()
            FileProvider.getUriForFile(context, authority, destinyFile)
        }?.also { downloadedUri ->
            try {
                resolver.openOutputStream(downloadedUri).use { outputStream ->
                    val brr = ByteArray(1024)
                    var len: Int
                    val bufferedInputStream =
                        BufferedInputStream(FileInputStream(downloadedFile.file.absoluteFile))
                    while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                        outputStream?.write(brr, 0, len)
                    }
                    outputStream?.flush()
                    bufferedInputStream.close()
                }
            } catch (e: Exception) {
                if (!SDKUtils.isBuildLargerThan(Build.VERSION_CODES.Q)) {
                    var destinyFile = File(
                        Environment.getExternalStoragePublicDirectory(publicFolder),
                        downloadedFile.file.name
                    )
                    if (destinyFile.exists()) {
                        var index = 1
                        var newPath =
                            "${destinyFile.parent}/${destinyFile.nameWithoutExtension} ($index).${destinyFile.extension}"
                        destinyFile = File(newPath)
                        while (destinyFile.exists()) {
                            index++
                            newPath =
                                "${destinyFile.parent}/${destinyFile.nameWithoutExtension} ($index).${destinyFile.extension}"
                            destinyFile = File(newPath)
                        }
                    }
                    downloadedFile.file.copyTo(destinyFile)
                    onFinish.invoke(destinyFile.absolutePath)
                }
            }
        }
    }

    fun copySupportedMediaPath(
        uri: Uri?,
        internalPath: String,
        context: Context = getApplication()
    ): String {
        if (uri == null) return ""
        val contentResolver = context.contentResolver ?: return ""

        val file = File(internalPath)
        val inputStream = contentResolver.openInputStream(uri) ?: return ""
        val outputStream = FileOutputStream(file)
        try {
            val buff = ByteArray(1024)
            var len = inputStream.read(buff)
            while (len > 0) {
                outputStream.write(buff, 0, len)
                len = inputStream.read(buff)
            }

        } catch (e: Exception) {
            return ""
        } finally {
            inputStream.close()
            outputStream.close()
        }
        return file.absolutePath
    }


    //region media

    fun File.getAudioDuration(): Long {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(absolutePath)
        val durationStr: String =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""
        val millSecond = durationStr.toLongOrNull() ?: 0L
        mmr.release()
        return millSecond
    }

    //endregion
}