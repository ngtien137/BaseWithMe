package com.base.baselibrary.adapter.function

import androidx.annotation.IdRes

annotation class SuperActionMenu(
    @IdRes val menuId: Int = -1,
    @IdRes val menuMainContent: Int = -1
) {
}