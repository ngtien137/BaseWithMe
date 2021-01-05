package com.base.baselibrary.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.base.baselibrary.R
import com.base.baselibrary.views.ext.loge
import java.lang.Exception

open class AppBaseDialog<T : ViewDataBinding>(
    protected val context: Context,
    @LayoutRes private val layoutId: Int,
    private val onInit: (binding: T) -> Unit = { },
    @StyleRes private val styleRes: Int = -1
) {

    protected var binding: T
    protected var dialog: AlertDialog

    open fun isHaveDim() = false

    init {
        val builder = if (styleRes == -1) AlertDialog.Builder(context) else AlertDialog.Builder(
            context,
            styleRes
        )
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        builder.setView(binding.root)
        binding.root.findViewById<View>(R.id.rootDialog)?.setOnClickListener {
            hide()
        }
        binding.lifecycleOwner = context as LifecycleOwner
        dialog = builder.create()
        onInit(binding)
        initView()
        setUpLayout()
    }

    private fun setUpLayout() {
        with(dialog) {
            if (window != null) {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                try {
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    if (!isHaveDim()) {
                        window?.setDimAmount(0.0f)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window?.let {
                                var flags: Int = window!!.decorView.systemUiVisibility
                                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                window?.decorView?.systemUiVisibility = flags
                            }
                        }
                    }
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun initView() {

    }

    fun configurePosition(x: Int, y: Int, gravity: Int = Gravity.NO_GRAVITY) {
        if (x == -1 && y == -1)
            return
        dialog.window?.let {
            val wmlp = it.attributes
            wmlp.gravity = gravity
            wmlp.x = x
            wmlp.y = y
        }
    }

    fun show() {
        dialog.show()
    }

    fun hide() {
        dialog.hide()
    }

}