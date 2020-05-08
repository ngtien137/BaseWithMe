package com.base.baselibrary.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.views.ext.loge
import com.base.baselibrary.views.ext.toast

abstract class BaseActivity<BD : ViewDataBinding> : AppCompatActivity() {

    private val listNotAbleGrantPermissions by lazy{
        ArrayList<String>().apply {
            add(Manifest.permission.MODIFY_PHONE_STATE)
        }
    }
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
            onDenied?.invoke()
        }
    }

    fun showMessage(resId: Int) {
        toast(resId)
    }

    fun showMessage(message: String) {
        toast(message)
    }
}