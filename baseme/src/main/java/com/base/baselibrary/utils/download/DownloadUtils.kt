package com.base.baselibrary.utils.download

import com.base.baselibrary.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object DownloadUtils {

    private const val BUFFER_LENGTH_BYTES = 1024 * 8
    private const val HTTP_TIMEOUT = 30L

    private val okHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
        OkHttpClient.Builder()
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(HTTP_TIMEOUT, TimeUnit.MINUTES) // write timeout
            .readTimeout(HTTP_TIMEOUT, TimeUnit.MINUTES) // read timeout
            .addInterceptor(interceptor)
            .build()
    }

    fun downloadFile(
        url: String,
        outputPath: String,
        onProgress: (progress: Int) -> Unit
    ): Throwable? {
        val fileOutput = File(outputPath)
        val request = Request.Builder().url(url).build()
        var error: Throwable? = null
        try {
            val response = okHttpClient.newCall(request).execute()
            val body = response.body
            val responseCode = response.code
            if (responseCode >= HttpURLConnection.HTTP_OK &&
                responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                body != null
            ) {
                body.byteStream().apply {
                    fileOutput.outputStream().use { fileOut ->
                        var bytesCopied = 0
                        val buffer = ByteArray(BUFFER_LENGTH_BYTES)
                        var bytes = read(buffer)
                        while (bytes >= 0) {
                            fileOut.write(buffer, 0, bytes)
                            bytesCopied += bytes
                            bytes = read(buffer)
                            val progressPercent =
                                (((bytesCopied * 100) / body.contentLength()).toInt())
                            onProgress.invoke(progressPercent)
                        }
                    }
                }

            } else {
                // Report the error
                error = DownloadException(responseCode)
            }
        } catch (e: Exception) {
            error = e
        }
        return error
    }
}