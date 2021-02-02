package com.lhd.view.basewithme.ui.dialog

import android.view.View
import android.view.ViewGroup
import com.base.baselibrary.dialog.BaseBindingBlurDialog
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.DialogBlurClipSizeBinding
import com.lhd.view.basewithme.databinding.DialogBlurFullSizeBinding

class DialogClipSizeBlur : BaseBindingBlurDialog<DialogBlurClipSizeBinding>() {

    override fun setUp(view: View?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_blur_clip_size
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.rootDialog
    }

    override fun getBlurBackgroundView(): View {
        return binding.rootBlurDialog
    }

    override fun isFullScreenWidth(): Boolean {
        return true
    }

    override fun isClipBlurBackground(): Boolean {
        return true
    }

    override fun animateDialog(viewGroup: ViewGroup?) {
        animateDialogWithAnimResourceId(R.anim.anim_slide_from_bottom)
    }

}