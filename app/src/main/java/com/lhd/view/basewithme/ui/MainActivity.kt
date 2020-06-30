package com.lhd.view.basewithme.ui

import androidx.activity.viewModels
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.viewmodel.MultiParamsFactory
import com.base.baselibrary.viewmodel.autoViewModels
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.ActivityMainBinding
import com.lhd.view.basewithme.viewmodel.HomeViewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    //private val viewModels by autoViewModels<HomeViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}
