package com.base.baselibrary.utils

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.isConnectedInternet(): Boolean {
    return InternetConnection.checkConnection(context!!)
}

fun Activity.isConnectedInternet(): Boolean {
    return InternetConnection.checkConnection(this)
}