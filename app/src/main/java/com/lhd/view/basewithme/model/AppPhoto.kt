package com.lhd.view.basewithme.model

import android.net.Uri
import android.provider.MediaStore
import com.base.baselibrary.utils.media_provider.MediaInfo
import java.util.*

data class AppPhoto(
    @MediaInfo(MediaStore.Files.FileColumns._ID)
    override var id: Long = -1,
    @MediaInfo(MediaStore.Files.FileColumns.DATA)
    override var path: String = "",
    @MediaInfo(MediaStore.Files.FileColumns.DATE_MODIFIED)
    override var date: Long = Date().time
) : AppMedia(id, path, date) {
    override fun getUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

}


