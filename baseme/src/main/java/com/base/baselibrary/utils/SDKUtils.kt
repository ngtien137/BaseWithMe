package com.base.baselibrary.utils

import android.os.Build

object SDKUtils {
    fun isBuildLargerThan(versionCode: Int) = Build.VERSION.SDK_INT >= versionCode

    fun onCheckVersion(onBelowQ: () -> Unit = {}, onAboveQ: () -> Unit = {}) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onAboveQ()
        } else {
            onBelowQ()
        }
    }
}