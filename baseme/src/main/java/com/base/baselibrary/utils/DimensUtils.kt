package com.base.baselibrary.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

object DimensUtils {
    fun convertDpToPixel(dp: Float): Int {
        val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    fun hasNotch(context: Context): Boolean {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        val barHeight = convertDpToPixel(24f)

        return statusBarHeight > barHeight
    }
}