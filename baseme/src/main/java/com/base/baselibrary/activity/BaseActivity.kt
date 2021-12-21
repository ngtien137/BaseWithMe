package com.base.baselibrary.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.utils.ActivityUtils
import com.base.baselibrary.utils.ActivityUtils.getResultLauncherForIntent
import com.base.baselibrary.utils.ActivityUtils.openAppSetting

abstract class BaseActivity<BD : ViewDataBinding> : AppCompatActivity() {

    private val listNotAbleGrantPermissions by lazy {
        ArrayList<String>().apply {
            add(Manifest.permission.MODIFY_PHONE_STATE)
        }
    }
    private var isFullScreen: Boolean = false
    private val REQUEST_PERMISSION = 1
    private val REQUEST_APP_SETTING = 10
    private var REQUEST_CUSTOM_INTENT = 137

    protected lateinit var binding: BD
    private var onAllow: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null
    private var onResult: (resultCode: ActivityResult) -> Unit = {}
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        resultLauncher = getResultLauncherForIntent {
            onResult.invoke(it)
        }
        if (isOnlyPortraitScreen())
            setPortraitScreen()
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

    open fun isOnlyPortraitScreen() = true

    open fun fixSingleTask(): Boolean = false

    fun checkPermission(
        permissions: Array<String>
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

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitScreen() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }
    }

    private fun isBelongToNotAbleGrantPermission(permission: String): Boolean {
        if (listNotAbleGrantPermissions.contains(permission))
            return true
        return false
    }

    fun doRequestPermission(
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

    fun openAppSetting(onResult: (result: ActivityResult) -> Unit = {}) {
        this.onResult = onResult
        openAppSetting(this, resultLauncher)
    }

    open fun onPermissionRejectAbsolute() {
        //Permisisions Don't ask again
    }

    //region save action
    private var isKeepSafeAction = false
    private var safeAction: (() -> Unit)? = null
    var isPause = false

    fun doSafeAction(action: () -> Unit) {
        if (!isPause) {
            action()
        } else {
            safeAction = action
            isKeepSafeAction = true
        }
    }

    override fun onPause() {
        isPause = true
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        if (isKeepSafeAction) {
            safeAction?.invoke()
            safeAction = null
            isKeepSafeAction = false
        }
    }

    //endregion

    //region intent with result


    //endregion

}