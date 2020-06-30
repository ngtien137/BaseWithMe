package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.ViewModel
import com.base.baselibrary.viewmodel.Auto
import com.lhd.view.basewithme.repository.DataRepository

class ListFragmentViewModel @Auto constructor(private val dataRepository: DataRepository) : ViewModel(){
    val dataTest by lazy{
        dataRepository.dataTest
    }
}