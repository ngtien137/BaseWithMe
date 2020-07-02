package com.lhd.view.basewithme.adapter

import com.base.baselibrary.adapter.ListItemListener
import com.lhd.view.basewithme.model.Account

interface IAccountListener : ListItemListener {
    fun onAccountClick(account:Account)
}