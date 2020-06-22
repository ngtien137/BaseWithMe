package com.base.baselibrary.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.utils.openAppSetting
import com.base.baselibrary.views.ext.loge
import com.base.baselibrary.views.ext.toast

abstract class BaseActivity<BD : ViewDataBinding> : AppCompatActivity() {

    private val listNotAbleGrantPermissions by lazy{
        ArrayList<String>().apply {
            add(Manifest.permission.MODIFY_PHONE_STATE)
        }
    }
    private var isFullScreen: Boolean = false
    private val REQUEST_PERMISSION = 1

    protected lateinit var binding: BD
    private var onAllow: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        initView()
    }

    protected open fun initView() {}

    protected open fun isOpenSettingIfCheckNotAskAgainPermission() = true

    abstract fun getLayoutId(): Int

    fun checkPermission(permissions: Array<String>,isSkipSpecialPermission:Boolean=false): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (checkSelfPermission(it) ==
                        PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    private fun isBelongToNotAbleGrantPermission(permission:String): Boolean {
        if (listNotAbleGrantPermissions.contains(permission))
            return true
        return false
    }

    protected fun doRequestPermission(permissions: Array<String>,
                                      onAllow: () -> Unit = {}, onDenied: () -> Unit = {}) {
        this.onAllow = onAllow
        this.onDenied = onDenied
        if (checkPermission(permissions)) {
            onAllow()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkPermission(permissions)) {
            onAllow?.invoke()
        } else {
            if (isOpenSettingIfCheckNotAskAgainPermission()) {
                for (i in permissions.indices) {
                    val rationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(permissions[i])
                    } else {
                        false
                    }
                    if (!rationale && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        openAppSetting(requestCode)
                        return
                    }
                }
            }
            onDenied?.invoke()
        }
    }

    fun changeFullscreenMode(isEnable: Boolean) {
        window?.apply {
            if (isEnable) {
                isFullScreen = true
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
            } else if (!isEnable) {
                isFullScreen = false
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
            }
        }
    }
}