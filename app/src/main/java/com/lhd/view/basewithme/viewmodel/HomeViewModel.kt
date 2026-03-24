package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.ViewModel
import com.lhd.view.basewithme.repository.DataRepository
import com.lhd.view.basewithme.repository.HomeRepository

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val dataRepository: DataRepository
) : ViewModel() {
}
