package com.base.baselibrary.dialog

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.base.baselibrary.utils.cache.BitmapCache
import kotlin.math.roundToInt

abstract class BaseBindingBlurDialog<BD : ViewDataBinding> : BaseBindingFragmentDialog<BD>() {

    private lateinit var blurView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(context), layoutId, null, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        initBinding()
        val background = _binding!!.root.findViewById<View>(R.id.backgroundDialog)
        background?.setOnClickListener { dismiss() }
        return _binding!!.root
    }

    override fun isFullScreenHeight(): Boolean {
        return true
    }

    override fun isFullScreenWidth(): Boolean {
        return true
    }

    open fun getBlurBackgroundView(): View? {
        return _binding!!.root.findViewById(R.id.backgroundDialog)
    }

    open fun isClipBlurBackground(): Boolean {
        return false
    }

    open fun getBlurRadius() = 6f

    open fun getBitmapCache(): Bitmap? {
        val keyCache = this::class.java.simpleName
        return BitmapCache.getBitmap(keyCache)
    }

    override fun initBinding() {
        if (getBlurBackgroundView() != null) {
            if (::blurView.isInitialized) {
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
                                newBitmap.blurBitmap(getBlurRadius(), context)
                                    ?.toDrawable(resources)
                            //val resultBitmap = Bitmap.createBitmap()
                        }
                    }
                } else {
                    val bitmap = blurView.drawViewToBitmap(0.2f)
                    getBlurBackgroundView()?.background =
                        bitmap?.blurBitmap(getBlurRadius(), context)?.toDrawable(resources)
                }
            } else {
                val bitmap =
                    getBitmapCache()?.blurBitmap(getBlurRadius(), context)?.toDrawable(resources)
                getBlurBackgroundView()?.background = bitmap
            }
        }
    }


    fun showWithBlur(fragmentManager: FragmentManager, blurView: View) {
        this.blurView = blurView
        show(fragmentManager)
    }

    override fun onDestroy() {
        BitmapCache.clearBitmap(this::class.java.simpleName)
        super.onDestroy()
    }


}