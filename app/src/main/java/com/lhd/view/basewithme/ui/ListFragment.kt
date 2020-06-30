package com.lhd.view.basewithme.ui

import androidx.fragment.app.viewModels
import com.base.baselibrary.viewmodel.AutoFactory
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.loge
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.FragmentListBinding
import com.lhd.view.basewithme.viewmodel.ListFragmentViewModel

class ListFragment : BaseNavFragment<FragmentListBinding>() {

    val viewModel: ListFragmentViewModel by autoViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    override fun initBinding() {

    }

    override fun initView() {
        val dataTest = viewModel.dataTest.value?:ArrayList()
        loge("Data Test Size: ${dataTest.size}")
        loge("=====================")
        for (item in dataTest){
            loge("Item: $item")
        }
        loge("=====================")
    }

    override fun onViewClick(vId: Int) {
        when (vId) {

        }
    }

}