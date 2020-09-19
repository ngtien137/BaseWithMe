package com.lhd.view.basewithme.adapter.simple

import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.model.Account

class CustomBaseAdapter : BaseAdapter<Account>(R.layout.item_account_simple) {

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        super.onBindViewHolder(holder, position)
        //custom here
    }
}