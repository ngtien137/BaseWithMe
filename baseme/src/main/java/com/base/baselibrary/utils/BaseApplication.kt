package com.base.baselibrary.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.base.baselibrary.utils.app_module.BaseSharedPreferences
import com.base.baselibrary.utils.data_store.BaseDataStore
import com.base.baselibrary.utils.share_preference.SharePreferencesUtils.initPrefData

open class BaseApplication : Application() {

    lateinit var baseDataStore: BaseDataStore

    override fun onCreate() {
        super.onCreate()
        if (isNoDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        initBaseApplication()
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is BaseSharedPreferences -> {
                    if (annotation.name.isNotEmpty()) {
                        baseDataStore = BaseDataStore(this, annotation.name)
                        initPrefData(annotation.name)
                    }
                }
                else -> {
                }
            }
        }
    }

    open fun isNoDarkMode(): Boolean = true

}