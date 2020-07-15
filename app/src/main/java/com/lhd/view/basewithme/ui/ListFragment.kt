package com.lhd.view.basewithme.ui

import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.loge
import com.base.baselibrary.views.ext.toast
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.AccountAdapter
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
        AccountAdapter().apply {
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
        observer(adapter.liveListSelected){
            loge("Selected Size: #${it?.size}, $it")
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
            R.id.btnCloseMenu -> {
                adapter.closeAllActionMenu()
            }
        }
    }

    override fun onDeleteAccount(account: Account, itemPosition: Int) {
        viewModel.dataTest.value?.remove(account)
        adapter.notifyItemRemoved(itemPosition)
        adapter.notifyItemRangeChanged(itemPosition, adapter.itemCount - itemPosition)
    }

    override fun onItemSelected(item: Account, position: Int, selected: Boolean) {
        loge("Item check: $item: $selected")
    }

    override fun onViewHandleCheckClicked(item: Account, position: Int) {
        toast("Item Clicked: #$position ")
    }

}