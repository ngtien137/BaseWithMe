package com.base.baselibrary.fragment

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.fragment.BaseFragment

abstract class BaseNavigationFragment<BD : ViewDataBinding, A : BaseActivity<*>> :
    BaseFragment<BD, A>() {

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