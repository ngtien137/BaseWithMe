package com.base.baselibrary.adapter.function

import androidx.annotation.IdRes

annotation class SuperSelect(
    @IdRes val viewHandleSelect: Int = -1,
    val handleByLongClick: Boolean = false,
    val enableUnSelect: Boolean = true,
    val enableMultiSelect: Boolean = false
) {
}