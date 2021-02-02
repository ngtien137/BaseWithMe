package com.base.baselibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.base.baselibrary.R
import com.base.baselibrary.utils.bitmap.blurBitmap
import com.base.baselibrary.utils.bitmap.drawViewToBitmap

abstract class BaseBindingBlurDialog<BD : ViewDataBinding> : BaseBindingFragmentDialog<BD>() {

    private lateinit var blurView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(context), layoutId, null, false)
        initBinding()
        val background = binding.root.findViewById<View>(R.id.backgroundDialog)
        background?.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun isFullScreenHeight(): Boolean {
        return true
    }

    override fun isFullScreenWidth(): Boolean {
        return true
    }

    open fun getBlurBackgroundView(): View? {
        return null
    }

    override fun initBinding() {
        val bitmap = blurView.drawViewToBitmap(0.2f)
        getBlurBackgroundView()?.background =
            bitmap?.blurBitmap(18f, context)?.toDrawable(resources)
    }

    fun showWithBlur(fragmentManager: FragmentManager, blurView: View) {
        this.blurView = blurView
        show(fragmentManager)
    }


}