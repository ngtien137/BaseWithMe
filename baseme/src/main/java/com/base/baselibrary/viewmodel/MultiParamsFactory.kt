package com.base.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class MultiParamsFactory constructor(protected vararg val params: Any) :
ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val listClass = params.map { it.javaClass }.toTypedArray()
        val constructor = modelClass.getDeclaredConstructor(*listClass)
        return constructor.newInstance(*params)
    }
}