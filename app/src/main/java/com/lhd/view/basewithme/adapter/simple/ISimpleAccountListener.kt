package com.lhd.view.basewithme.adapter.simple

import com.base.baselibrary.adapter.listener.ListItemListener
import com.lhd.view.basewithme.model.Account

interface ISimpleAccountListener : ListItemListener {
    fun onSimpleAccountClick(item: Account, position: Int)

    fun onDoingSomethingAbcXyz()
}