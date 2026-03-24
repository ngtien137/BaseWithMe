package com.base.baselibrary.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

interface IAbstractClickView{
    @SuppressLint("PrivateResource")
    fun setBackground(context: Context, view: View, background: Drawable?) {
//        if (!view.isClickable) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var bg = background
            if (bg == null) {
                val outValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                bg = context.getDrawable(outValue.resourceId)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    val colorValue = TypedValue()
                    val hasColor = context.theme.resolveAttribute(
                        androidx.appcompat.R.attr.colorControlHighlight,
                        colorValue,
                        true
                    )
                    val rippleColor = when {
                        !hasColor -> 0
                        colorValue.resourceId != 0 -> ContextCompat.getColor(context, colorValue.resourceId)
                        else -> colorValue.data
                    }
                    val ripple = RippleDrawable(ColorStateList.valueOf(rippleColor), bg, null)
                    view.background = ripple
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
