package com.lhd.view.basewithme.ui

import androidx.fragment.app.viewModels
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.viewmodel.MultiParamsFactory
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.FragmentHomeBinding
import com.lhd.view.basewithme.repository.DataRepository
import com.lhd.view.basewithme.repository.HomeRepository
import com.lhd.view.basewithme.viewmodel.HomeViewModel

class HomeFragment : BaseNavFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels {
        MultiParamsFactory(HomeRepository(), DataRepository())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setHandleBack(): Boolean {
        return false
    }

    override fun initBinding() {
        binding.viewListener = this
        binding.viewModel = viewModel
    }

    override fun initView() {

    }

    override fun getParentActivity(): BaseActivity<*>? {
        return rootActivity
    }

}
