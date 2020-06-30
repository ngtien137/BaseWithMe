package com.lhd.view.basewithme.repository

import androidx.lifecycle.MutableLiveData
import com.lhd.view.basewithme.model.Account

class DataRepository {
    val dataTest by lazy {
        MutableLiveData(ArrayList<Account>())
    }
}