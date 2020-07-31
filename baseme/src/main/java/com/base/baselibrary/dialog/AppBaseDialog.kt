package com.base.baselibrary.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.base.baselibrary.views.ext.loge
import java.lang.Exception

open class AppBaseDialog<T : ViewDataBinding>(
    protected val context: Context,
    @LayoutRes private val layoutId: Int
) {

    protected var binding: T
    protected var dialog: AlertDialog

    open fun isHaveDim() = false

    init {
        val builder = AlertDialog.Builder(context)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        builder.setView(binding.root)
        binding.lifecycleOwner = context as LifecycleOwner
        dialog = builder.create()
        setUpLayout()
    }

    private fun setUpLayout() {
        with(dialog) {
            if (window != null) {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                try {
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    if (!isHaveDim())
                        window?.setDimAmount(0.0f)
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
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