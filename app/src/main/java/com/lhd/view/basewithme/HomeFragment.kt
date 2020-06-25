package com.lhd.view.basewithme

import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.getSingleton
import com.base.baselibrary.utils.post
import com.base.baselibrary.viewmodel.MultiParamsFactory
import com.base.baselibrary.views.ext.toast
import com.lhd.view.basewithme.databinding.FragmentHomeBinding
import com.lhd.view.basewithme.repository.HomeRepository
import com.lhd.view.basewithme.viewmodel.HomeViewModel

class HomeFragment : BaseNavFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels {
        MultiParamsFactory(HomeRepository::class.java.getSingleton())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initBinding() {
        binding.viewListener = this
        binding.viewModel = viewModel
    }

    override fun initView() {

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
        }
    }

}