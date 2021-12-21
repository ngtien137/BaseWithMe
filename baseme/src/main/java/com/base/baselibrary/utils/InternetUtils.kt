package com.base.baselibrary.utils

import android.app.Activity
import androidx.fragment.app.Fragment

object InternetUtils {

    fun Fragment.isConnectedInternet(): Boolean? {
        if (context == null)
            return null
        return InternetConnection.checkConnection(context!!)
    }

    fun Activity.isConnectedInternet(): Boolean {
        return InternetConnection.checkConnection(this)
    }

}