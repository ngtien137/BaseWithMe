package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PagerTestViewModel : ViewModel() {

    val listTitle by lazy {
        MutableLiveData(arrayListOf<String>().apply {
            for (i in 1..10) {
                add("Tab$i")
            }
        })
    }

}
