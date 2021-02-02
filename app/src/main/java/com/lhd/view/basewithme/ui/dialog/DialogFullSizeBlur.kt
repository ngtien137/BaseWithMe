package com.lhd.view.basewithme.ui.dialog

import android.view.View
import android.view.ViewGroup
import com.base.baselibrary.dialog.BaseBindingBlurDialog
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.DialogBlurFullSizeBinding

class DialogFullSizeBlur : BaseBindingBlurDialog<DialogBlurFullSizeBinding>() {

    override fun setUp(view: View?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_blur_full_size
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.rootCard
    }

    override fun getBlurBackgroundView(): View? {
        return binding.rootBlurDialog
    }
}