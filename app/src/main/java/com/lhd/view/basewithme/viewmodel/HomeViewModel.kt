package com.lhd.view.basewithme.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.Event
import com.lhd.view.basewithme.repository.HomeRepository

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    var eventLoading = MutableLiveData(Event())

    fun showLoading(){
        eventLoading.post(true)
    }

    fun hideLoading(){
        eventLoading.post(false)
    }

    fun hideLoadingWithView(v:View?){
        eventLoading.post(false)
    }

}