package com.base.baselibrary.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController

interface INavigationAction {
    fun finishActivity() {
    }

    fun navigateUp() {
    }

    fun navigateTo(actionId: Int) {
    }

    fun navigateTo(actionId: Int, bundle: Bundle) {
    }

    fun popBackStack(navigationId: Int, popIdFragment: Boolean) {
    }

    fun popBackStack(navigationId: Int){}
}