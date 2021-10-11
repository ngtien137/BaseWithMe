package com.base.baselibrary.utils

import android.view.View
import androidx.databinding.BindingConversion

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}
