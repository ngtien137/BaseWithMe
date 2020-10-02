package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.viewmodel.Auto

class PagerTestViewModel @Auto private constructor() : ViewModel() {

    val listTitle by lazy {
        MutableLiveData(arrayListOf<String>().apply {
            for (i in 1..10) {
                add("Tab$i")
            }
        })
    }

}