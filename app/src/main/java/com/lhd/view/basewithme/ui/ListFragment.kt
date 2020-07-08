package com.lhd.view.basewithme.ui

import com.base.baselibrary.adapter.BaseSelectedAdapter
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.loge
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.IAccountListener
import com.lhd.view.basewithme.databinding.FragmentListBinding
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.viewmodel.ListFragmentViewModel

class ListFragment : BaseNavFragment<FragmentListBinding>(), IAccountListener {

    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    private val viewModel: ListFragmentViewModel by autoViewModels()

    private val adapter by lazy {
        (object : BaseSelectedAdapter<Account>(R.layout.item_account) {
            override fun getViewHandleSelected(): Int {
                return R.id.imgAccount
            }

            override fun enableMultipleSelected(): Boolean {
                return true
            }

            override fun enableUnSelected(): Boolean {
                return false
            }
        }).apply {
            listener = this@ListFragment
        }
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
            R.id.btnClearSelect -> {
                adapter.clearAllSelect()
            }
            R.id.btnSelectAll -> {
                adapter.selectedAll()
            }
        }
    }

    override fun onAccountClick(account: Account) {

    }

    override fun onItemSelected(item: Account, position: Int, selected: Boolean) {
        loge("Item check: $item: $selected")
    }

}