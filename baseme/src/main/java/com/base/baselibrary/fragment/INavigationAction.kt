package com.base.baselibrary.fragment

import android.os.Bundle
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.activity.BaseActivity

interface INavigationAction {
    fun finishActivity() {
    }

    fun navigateUp() {
    }

    fun navigateTo(actionId: Int) {
    }

    fun navigateTo(actionId: Int, bundle: Bundle) {
    }

    fun navigateTo(actionId:Int, direction: NavDirections){}

    fun popBackStack(navigationId: Int, popIdFragment: Boolean) {
    }

    fun popBackStack(navigationId: Int) {}

    fun getParentActivity(): BaseActivity<*>? {
        return null
    }

    fun navigateSingleTop(actionId: Int, popUpToId: Int = -1): Boolean {
        return false
    }
}