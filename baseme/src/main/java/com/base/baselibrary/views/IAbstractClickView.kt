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
import com.base.baselibrary.R

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
                    val ripple = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RippleDrawable(ColorStateList.valueOf(context.getColor(R.color.abc_color_highlight_material)), bg, null)
                    } else {
                        RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.abc_color_highlight_material)), bg, null)
                    }
                    view.background = ripple
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}