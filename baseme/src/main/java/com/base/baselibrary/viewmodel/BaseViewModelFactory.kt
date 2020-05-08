package com.base.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseViewModelFactory constructor(protected val params: Any) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val constructor = modelClass.getDeclaredConstructor(params::class.java)
        return constructor.newInstance(params)
    }
}