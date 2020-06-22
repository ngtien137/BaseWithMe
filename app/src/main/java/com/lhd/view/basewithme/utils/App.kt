package com.lhd.view.basewithme.utils

import android.app.Application
import com.base.baselibrary.utils.initBaseApplication

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initBaseApplication()
    }
}