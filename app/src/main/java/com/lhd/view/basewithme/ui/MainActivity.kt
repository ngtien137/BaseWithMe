package com.lhd.view.basewithme.ui

import android.Manifest
import com.base.baselibrary.activity.BaseActivity
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val listPermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    fun grantPermission(onAllow: () -> Unit) {
        doRequestPermission(listPermission, onAllow)
    }
}
