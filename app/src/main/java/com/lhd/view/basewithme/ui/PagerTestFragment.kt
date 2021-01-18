package com.lhd.view.basewithme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.base.baselibrary.views.ext.loge
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.pager.IPagerTabListener
import com.lhd.view.basewithme.adapter.pager.PagerTabAdapter
import com.lhd.view.basewithme.databinding.FragmentPagerTestBinding
import com.lhd.view.basewithme.viewmodel.PagerTestViewModel

class PagerTestFragment : BaseNavFragment<FragmentPagerTestBinding>(), IPagerTabListener {

    private val viewModel by autoViewModels<PagerTestViewModel>()

    private val adapter by lazy {
        BaseAdapter<String>(R.layout.item_pager_tab).apply {
            listener = this@PagerTestFragment
        }
    }

    private val listFragment by lazy {
        arrayListOf<TabFragment>().apply {
            for (i in 1..10) {
                add(TabFragment().apply { tab = "Tab$i" })
            }
        }
    }

    private val pagerAdapter by lazy {
        PagerTabAdapter(rootActivity.supportFragmentManager, listFragment)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pager_test
    }

    override fun initBinding() {
        binding.adapter = adapter
    }

    override fun initView() {
        observer(viewModel.listTitle) {
            adapter.data = it
        }
        binding.pager.adapter = pagerAdapter
        binding.pager.post {
            pagerAdapter.notifyDataSetChanged()
        }
    }

    override fun onPagerTabClick(item: String, position: Int) {
        binding.pager.currentItem = position
    }

}