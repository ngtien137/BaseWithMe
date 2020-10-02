package com.lhd.view.basewithme.adapter.pager

import com.base.baselibrary.adapter.listener.ListItemListener

interface IPagerTabListener : ListItemListener {
    fun onPagerTabClick(item: String, position: Int)
}