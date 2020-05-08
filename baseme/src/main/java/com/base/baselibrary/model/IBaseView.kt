package com.base.baselibrary.model

import androidx.annotation.StringRes

interface IBaseView {
    fun onShowLoading()

    fun onHideLoading()

    fun onShowMessage(mess: String)

    fun onShowMessage(@StringRes mess: Int)
}