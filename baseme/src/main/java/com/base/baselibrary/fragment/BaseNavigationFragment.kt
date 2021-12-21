package com.base.baselibrary.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.R
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.utils.bitmap.drawViewToBitmap
import com.base.baselibrary.utils.cache.BitmapCache

abstract class BaseNavigationFragment<BD : ViewDataBinding, A : BaseActivity<*>> :
    BaseFragment<BD, A>() {

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

    fun finishActivity() {
        rootActivity.finish()
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun navigateTo(actionId: Int, currentId: Int = -1) {
        if (currentId != -1) {
            if (findNavController().currentDestination?.id == currentId) {
                findNavController().navigate(actionId)
            }
        } else
            findNavController().navigate(actionId)
    }

    fun navigateTo(actionId: Int, bundle: Bundle) {
        findNavController().navigate(actionId, bundle)
    }

    fun navigateTo(viewId: Int, direction: NavDirections) {
        if (findNavController().currentDestination?.id == viewId) {
            findNavController().navigate(direction)
        }
    }

    fun navigateTo(viewId: Int, direction: NavDirections, extras: FragmentNavigator.Extras) {
        if (findNavController().currentDestination?.id == viewId) {
            findNavController().navigate(direction, extras)
        }
    }

    fun navigateUp(navId: Int) {
        if (findNavController().currentDestination?.id == navId) {
            navigateUp()
        }
    }

    fun popBackStack(navigationId: Int, popIdFragment: Boolean) {
        findNavController().popBackStack(navigationId, popIdFragment)
    }

    fun popBackStack(navigationId: Int) {
        findNavController().popBackStack(navigationId, false)
    }

    fun navigateSingleTop(actionId: Int, popUpToId: Int): Boolean {
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

    fun applyBlurBitmap(key: String, view: View, scale: Float = 0.2f) {
        val bitmap = view.drawViewToBitmap(scale)
        BitmapCache.addBitmap(key, bitmap)
    }

}