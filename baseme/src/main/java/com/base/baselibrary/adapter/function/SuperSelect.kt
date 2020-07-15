package com.base.baselibrary.adapter.function

import androidx.annotation.IdRes

annotation class SuperSelect(
    @IdRes val viewHandleSelectId: Int = -1,
    val handleByLongClick: Boolean = true,
    val enableUnSelect: Boolean = true,
    val enableMultiSelect: Boolean = false,
    val enableSelectItemMultipleTime: Boolean = false,
    val disableSelectModeWhenEmpty: Boolean = true,
    val validCheckAgainAfterEnableSelectedByLongClick: Boolean = true
) {
}