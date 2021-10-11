package com.lhd.view.basewithme.ui

import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.viewmodel.autoViewModels
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.FragmentHomeBinding
import com.lhd.view.basewithme.viewmodel.HomeViewModel

class HomeFragment : BaseNavFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by autoViewModels()

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
