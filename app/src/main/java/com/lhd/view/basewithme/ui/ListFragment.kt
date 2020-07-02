package com.lhd.view.basewithme.ui

import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.IAccountListener
import com.lhd.view.basewithme.databinding.FragmentListBinding
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.viewmodel.ListFragmentViewModel

class ListFragment : BaseNavFragment<FragmentListBinding>(), IAccountListener {

    private val viewModel: ListFragmentViewModel by autoViewModels()

    private val adapter by lazy {
        BaseAdapter<Account>(R.layout.item_account).apply {
            listener = this@ListFragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    override fun initBinding() {
        binding.viewModel = viewModel
        binding.adapter = adapter
    }

    override fun initView() {
        observer(viewModel.dataTest) {
            adapter.data = it
        }
    }

    override fun onViewClick(vId: Int) {
        when (vId) {

        }
    }

    override fun onAccountClick(account: Account) {

    }

}