package com.lhd.view.basewithme.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.fragment.BaseFragment

abstract class BaseNavFragment<BD : ViewDataBinding> : BaseFragment<BD, MainActivity>() {

    open fun isFullScreenMode() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (setHandleBack())
            initEventBackStack()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initEventBackStack() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    fun changeFullscreenMode(isEnable: Boolean) {
        activity.changeFullscreenMode(isEnable)
    }

    override fun onResume() {
        super.onResume()
        activity.changeFullscreenMode(isFullScreenMode())
    }

    open fun setHandleBack() = true

    open fun onBackPressed() {
        navigateUp()
    }

    fun finishActivity() {
        activity.finish()
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    fun navigateTo(actionId: Int, bundle: Bundle) {
        findNavController().navigate(actionId, bundle)
    }

    fun popBackStack(navigationId: Int, popIdFragment: Boolean = false) {
        findNavController().popBackStack(navigationId, popIdFragment)
    }

}