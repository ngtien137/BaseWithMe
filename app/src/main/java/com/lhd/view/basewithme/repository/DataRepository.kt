package com.lhd.view.basewithme.repository

import androidx.lifecycle.MutableLiveData
import com.lhd.view.basewithme.model.Account

class DataRepository {
    val dataTest by lazy {
        MutableLiveData(ArrayList<Account>())
    }

    init {
        initDataTest()
    }

    fun initDataTest(){
        /**
         * init data in home fragment then show it in list fragment
         */
        dataTest.value?.let {
            it.add(Account(it.size,"Account ${it.size}"))
            it.add(Account(it.size,"Account ${it.size}"))
            it.add(Account(it.size,"Account ${it.size}"))
            it.add(Account(it.size,"Account ${it.size}"))
            it.add(Account(it.size,"Account ${it.size}"))
        }
    }
}