package com.lhd.view.basewithme.adapter

import com.base.baselibrary.adapter.listener.IBaseSelectedAdapter
import com.lhd.view.basewithme.model.Account

interface IAccountListener : IBaseSelectedAdapter<Account> {
    fun onDeleteAccount(account: Account, itemPosition: Int){}
}