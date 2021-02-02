package com.base.baselibrary.dialog

import android.graphics.Bitmap
import android.graphics.Rect
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
import kotlin.math.roundToInt

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

    open fun isClipBlurBackground(): Boolean {
        return false
    }

    override fun initBinding() {
        if (::blurView.isInitialized && getBlurBackgroundView() != null) {
            if (isClipBlurBackground()) {
                getBlurBackgroundView()?.let { bgBlur ->
                    bgBlur.post {
                        val scaleRatio = 0.2f
                        val bitmap = blurView.drawViewToBitmap(scaleRatio)!!
                        val newBitmap = Bitmap.createBitmap(
                            bitmap,
                            0,
                            (bitmap.height - bgBlur.height * scaleRatio).roundToInt(),
                            (bgBlur.width * scaleRatio).roundToInt(),
                            (bgBlur.height * scaleRatio).roundToInt()
                        )
                        getBlurBackgroundView()?.background =
                            newBitmap.blurBitmap(18f, context)?.toDrawable(resources)
                        //val resultBitmap = Bitmap.createBitmap()
                    }
                }
            } else {
                val bitmap = blurView.drawViewToBitmap(0.2f)
                getBlurBackgroundView()?.background =
                    bitmap?.blurBitmap(18f, context)?.toDrawable(resources)
            }
        }
    }

    fun showWithBlur(fragmentManager: FragmentManager, blurView: View) {
        this.blurView = blurView
        show(fragmentManager)
    }


}