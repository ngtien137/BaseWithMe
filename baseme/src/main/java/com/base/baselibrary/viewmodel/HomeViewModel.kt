package com.base.baselibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel @Auto private constructor() : ViewModel() {

    val liveTabIndex = MutableLiveData(0)

}