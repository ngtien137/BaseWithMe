package com.lhd.view.basewithme.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.fragment.BaseFragment
import com.base.baselibrary.fragment.INavigationAction

abstract class BaseNavFragment<BD : ViewDataBinding> : BaseFragment<BD, MainActivity>(),
    INavigationAction {

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    fun changeFullscreenMode(isEnable: Boolean) {
        rootActivity.changeFullscreenMode(isEnable)
    }

    override fun onResume() {
        super.onResume()
        rootActivity.changeFullscreenMode(isFullScreenMode())
    }

    open fun setHandleBack() = true

    open fun onBackPressed() {
        navigateUp()
    }

    override fun finishActivity() {
        rootActivity.finish()
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun navigateTo(actionId: Int, bundle: Bundle) {
        findNavController().navigate(actionId, bundle)
    }

    override fun popBackStack(navigationId: Int, popIdFragment: Boolean) {
        findNavController().popBackStack(navigationId, popIdFragment)
    }

    override fun popBackStack(navigationId: Int) {
        findNavController().popBackStack(navigationId, false)
    }

    override fun getParentActivity(): BaseActivity<*>? {
        return null
    }

}