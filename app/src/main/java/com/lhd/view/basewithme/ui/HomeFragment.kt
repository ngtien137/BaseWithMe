package com.lhd.view.basewithme.ui

import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.AutoFactory
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.toast
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
        viewModel.initDataTest()
    }

    override fun onViewClick(vId: Int) {
        when (vId) {
            R.id.btnTest -> {
                toast("Test")
            }
            R.id.btnShowLoading -> {
                viewModel.eventLoading.post(true)
            }
            R.id.btnCancelLoading -> {
                viewModel.eventLoading.post(false)
            }
            R.id.btnListFragment -> {
                navigateTo(R.id.action_homeFragment_to_listFragment)
            }
            R.id.btnBaseAdapter -> {
                navigateTo(R.id.action_homeFragment_to_baseAdapterFragment)
            }
        }
    }

}
