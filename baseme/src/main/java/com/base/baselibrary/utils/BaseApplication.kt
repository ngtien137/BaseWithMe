package com.base.baselibrary.utils

import android.app.Application
import com.base.baselibrary.utils.app_module.BaseSharedPreferences

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initBaseApplication()
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is BaseSharedPreferences -> {
                    if (annotation.name.isNotEmpty())
                        initPrefData(annotation.name)
                }
                else -> {}
            }
        }
    }
}