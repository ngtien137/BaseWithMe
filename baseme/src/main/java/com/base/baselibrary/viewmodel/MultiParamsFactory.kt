package com.base.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

open class MultiParamsFactory constructor(protected vararg val params: Any) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return createInstance(modelClass)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return createInstance(modelClass)
    }

    private fun <T : ViewModel> createInstance(modelClass: Class<T>): T {
        val listClass = params.map { it.javaClass }.toTypedArray()
        val constructor = modelClass.getDeclaredConstructor(*listClass)
        return constructor.newInstance(*params)
    }
}
