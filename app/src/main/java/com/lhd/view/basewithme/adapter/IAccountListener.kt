package com.lhd.view.basewithme.adapter

import com.base.baselibrary.adapter.listener.ISuperAdapterListener
import com.lhd.view.basewithme.model.Account

interface IAccountListener : ISuperAdapterListener<Account> {
    fun onDeleteAccount(account: Account, itemPosition: Int){}
}