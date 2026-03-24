package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhd.view.basewithme.model.LiveObject

class LiveObjectViewModel : ViewModel() {

    val liveListObject by lazy {
        MutableLiveData(ArrayList<LiveObject>().apply {
            add(LiveObject())
            add(LiveObject())
            add(LiveObject())
            add(LiveObject())
            add(LiveObject())
            add(LiveObject())
            add(LiveObject())
        })
    }

}
