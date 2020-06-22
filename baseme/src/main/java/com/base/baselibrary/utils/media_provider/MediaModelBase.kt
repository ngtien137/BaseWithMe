package com.base.baselibrary.utils.media_provider

import android.net.Uri
import java.io.Serializable

abstract class MediaModelBase : Serializable {
    abstract fun getUri(): Uri
}