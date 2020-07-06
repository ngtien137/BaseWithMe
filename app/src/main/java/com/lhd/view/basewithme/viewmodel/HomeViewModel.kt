package com.lhd.view.basewithme.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.Auto
import com.base.baselibrary.viewmodel.Event
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.repository.DataRepository
import com.lhd.view.basewithme.repository.HomeRepository

class HomeViewModel @Auto constructor(
    private val homeRepository: HomeRepository,
    private val dataRepository: DataRepository
) : ViewModel() {
    val dataTest by lazy{
        dataRepository.dataTest
    }

    fun initDataTest(){
        if (dataTest.value.isNullOrEmpty()){
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

}