package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.ViewModel
import com.lhd.view.basewithme.repository.DataRepository

class ListFragmentViewModel(private val dataRepository: DataRepository) : ViewModel() {
    val dataTest by lazy {
        dataRepository.dataTest
    }
}
