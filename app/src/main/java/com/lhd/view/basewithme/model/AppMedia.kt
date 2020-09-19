package com.lhd.view.basewithme.model

import com.base.baselibrary.utils.media_provider.MediaModelBase
import java.util.*

abstract class AppMedia(
    open var id: Long = -1,
    open var path: String = "",
    open var date: Long = Date().time
) : MediaModelBase(){
    override fun toString(): String {
        return "AppMedia(id=$id, path='$path', date=$date)"
    }
}