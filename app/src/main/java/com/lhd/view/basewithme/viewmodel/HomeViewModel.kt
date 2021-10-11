package com.lhd.view.basewithme.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.fragment.INavigationAction
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.Auto
import com.base.baselibrary.viewmodel.Event
import com.base.baselibrary.views.ext.toast
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.repository.DataRepository
import com.lhd.view.basewithme.repository.HomeRepository
import com.lhd.view.basewithme.ui.MainActivity

class HomeViewModel @Auto constructor(
    private val homeRepository: HomeRepository,
    private val dataRepository: DataRepository
) : ViewModel() {


}