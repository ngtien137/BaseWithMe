package com.lhd.view.basewithme.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.fragment.INavigationAction
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.Auto
import com.base.baselibrary.viewmodel.Event
import com.base.baselibrary.views.ext.toast
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.repository.DataRepository
import com.lhd.view.basewithme.repository.HomeRepository
import com.lhd.view.basewithme.ui.MainActivity

class HomeViewModel @Auto constructor(
    private val homeRepository: HomeRepository,
    private val dataRepository: DataRepository
) : ViewModel() {
    val dataTest by lazy {
        dataRepository.dataTest
    }

    var navigationListener: INavigationAction? = null

    fun initDataTest() {
        if (dataTest.value.isNullOrEmpty()) {
            dataRepository.initDataTest()
        }
    }

    var eventLoading = MutableLiveData(Event())

    fun showLoading() {
        eventLoading.post(true)
    }

    fun hideLoading() {
        eventLoading.post(false)
    }

    fun hideLoadingWithView(v: View?) {
        eventLoading.post(false)
    }

    fun onClick(view: View?) {
        view?.let {
            onViewClick(it.id)
        }
    }

    fun onViewClick(vId: Int) {
        when (vId) {
            R.id.btnTest -> {
                getApplication().toast("Test")
            }
            R.id.btnShowLoading -> {
                eventLoading.post(true)
            }
            R.id.btnCancelLoading -> {
                eventLoading.post(false)
            }
            R.id.btnListFragment -> {
                navigationListener?.navigateTo(R.id.action_homeFragment_to_listFragment)
            }
            R.id.btnBaseAdapter -> {
                navigationListener?.navigateTo(R.id.action_homeFragment_to_baseAdapterFragment)
            }
            R.id.btnMediaDemo -> {
                val activity = (navigationListener?.getParentActivity() as MainActivity?)
                activity?.grantPermission {
                    navigationListener?.navigateTo(R.id.action_homeFragment_to_mediaFragment)
                }
            }
            R.id.btnLiveObjectDemo -> {
                navigationListener?.navigateTo(R.id.action_homeFragment_to_listObjectLiveFragment)
            }
            R.id.btnPagerDemo -> {
                navigationListener?.navigateTo(R.id.action_homeFragment_to_pagerTestFragment)
            }
        }
    }

}