package com.lhd.view.basewithme.ui

import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.toast
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.simple.CustomBaseAdapter
import com.lhd.view.basewithme.adapter.simple.ISimpleAccountListener
import com.lhd.view.basewithme.databinding.FragmentBaseAdapterBinding
import com.lhd.view.basewithme.model.Account
import com.lhd.view.basewithme.viewmodel.ListFragmentViewModel

class BaseAdapterFragment : BaseNavFragment<FragmentBaseAdapterBinding>(), ISimpleAccountListener {

    private val viewModel: ListFragmentViewModel by autoViewModels()

    private val adapter by lazy {
        BaseAdapter<Account>(R.layout.item_account_simple).apply { // Create a adapter with generic type is Account, with layout item R.layout.item_account_simple
            listener = this@BaseAdapterFragment //Set a listener implement ListItemListener
        }
    }

    /**
     * if you want to custom a base adapter such as check item, payload,...
     * create a class extends it such as class CustomBaseAdapter in adapter.simple.CustomBaseAdapter
     */
//    private val adapter by lazy {
//        CustomBaseAdapter().apply {
//            listener = this@BaseAdapterFragment
//        }
//    }

    override fun initBinding() {
        binding.viewModel = viewModel
        binding.adapter = adapter
    }

    override fun initView() {
        observer(viewModel.dataTest) {
            adapter.data = it //Set data for adapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_base_adapter
    }

    override fun onSimpleAccountClick(item: Account, position: Int) {
        toast("Item click #${position}: $item")
    }

    override fun onDoingSomethingAbcXyz() {

    }

}