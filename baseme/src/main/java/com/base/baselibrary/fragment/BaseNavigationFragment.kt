package com.base.baselibrary.fragment

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.R
import com.base.baselibrary.activity.BaseActivity

abstract class BaseNavigationFragment<BD : ViewDataBinding, A : BaseActivity<*>> :
    BaseFragment<BD, A>(), INavigationAction {

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

    override fun navigateTo(viewId: Int, direction: NavDirections) {
        if (findNavController().currentDestination?.id == viewId) {
            findNavController().navigate(direction)
        }
    }

    override fun popBackStack(navigationId: Int, popIdFragment: Boolean) {
        findNavController().popBackStack(navigationId, popIdFragment)
    }

    override fun popBackStack(navigationId: Int) {
        findNavController().popBackStack(navigationId, false)
    }

    override fun navigateSingleTop(actionId: Int, popUpToId: Int): Boolean {
        val navController = findNavController()
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
        if (navController.currentDestination!!.parent!!.findNode(actionId) is ActivityNavigator.Destination) {
            builder.setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
        } else {
            builder.setEnterAnim(R.animator.nav_default_enter_anim)
                .setExitAnim(R.animator.nav_default_exit_anim)
                .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
        }
        if (popUpToId != -1) {
            builder.setPopUpTo(popUpToId, false)
        }
        val options = builder.build()
        return try {
            navController.navigate(actionId, null, options)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun navigateWithResult(
        actionId: Int,
        requestKey: String,
        onResult: (requestKey: String, bundle: Bundle) -> Unit = { _, _ -> }
    ) {
        navigateWithResult(actionId, Bundle(), requestKey, onResult)
    }

    fun navigateWithResult(
        actionId: Int, bundle: Bundle,
        requestKey: String,
        onResult: (requestKey: String, bundle: Bundle) -> Unit = { _, _ -> }
    ) {
        setFragmentResultListener(requestKey, onResult)
        navigateTo(actionId, bundle)
    }

}