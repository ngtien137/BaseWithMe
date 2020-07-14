package com.lhd.view.basewithme.adapter

import com.base.baselibrary.adapter.SuperAdapter
import com.base.baselibrary.adapter.function.SuperActionMenu
import com.base.baselibrary.adapter.function.SuperDragVertical
import com.base.baselibrary.adapter.function.SuperSelect
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.model.Account

@SuperActionMenu
@SuperDragVertical
@SuperSelect(viewHandleSelectId = R.id.imgAccount,enableMultiSelect = true)
class AccountAdapter : SuperAdapter<Account>(R.layout.item_account) {

}