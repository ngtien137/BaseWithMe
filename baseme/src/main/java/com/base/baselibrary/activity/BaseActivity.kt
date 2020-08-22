package com.base.baselibrary.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.utils.audio_focus.BaseAudioFocus
import com.base.baselibrary.utils.call_phone.BasePhoneState
import com.base.baselibrary.utils.openAppSetting

abstract class BaseActivity<BD : ViewDataBinding> : AppCompatActivity() {

    private val listNotAbleGrantPermissions by lazy {
        ArrayList<String>().apply {
            add(Manifest.permission.MODIFY_PHONE_STATE)
        }
    }
    private var isFullScreen: Boolean = false
    private val REQUEST_PERMISSION = 1
    private val REQUEST_APP_SETTING = 10

    protected lateinit var binding: BD
    private var onAllow: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    val livePhoneState by lazy {
        BasePhoneState.listenPhoneState(this)
        BasePhoneState.livePhoneState
    }

    val liveAudioFocus by lazy {
        BaseAudioFocus.liveAudioState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fixSingleTask()) {
            if (!isTaskRoot) {
                finish()
                return
            }
        }
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        initView()
    }

    protected open fun initView() {}

    protected open fun isOpenSettingIfCheckNotAskAgainPermission() = true

    abstract fun getLayoutId(): Int

    open fun fixSingleTask(): Boolean = false

    fun checkPermission(
        permissions: Array<String>,
        isSkipSpecialPermission: Boolean = false
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (checkSelfPermission(it) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun isBelongToNotAbleGrantPermission(permission: String): Boolean {
        if (listNotAbleGrantPermissions.contains(permission))
            return true
        return false
    }

    protected fun doRequestPermission(
        permissions: Array<String>,
        onAllow: () -> Unit = {}, onDenied: () -> Unit = {}
    ) {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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
                        onPermissionRejectAbsolute()
                        return
                    }
                }
            }
            onDenied?.invoke()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_APP_SETTING -> {
                onResultFromOpenAppSetting()
            }
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

    open fun onResultFromOpenAppSetting() {

    }

    open fun onPermissionRejectAbsolute() {
        //Permisisions Don't ask again
        openAppSetting()
    }

    fun openAppSetting() {
        openAppSetting(REQUEST_APP_SETTING)
    }
}