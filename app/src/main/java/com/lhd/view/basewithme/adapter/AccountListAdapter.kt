package com.lhd.view.basewithme.adapter

//import androidx.recyclerview.widget.DiffUtil
//import com.base.baselibrary.adapter.SuperAdapter
//import com.base.baselibrary.adapter.SuperListAdapter
//import com.base.baselibrary.adapter.function.SuperActionMenu
//import com.base.baselibrary.adapter.function.SuperDragVertical
//import com.base.baselibrary.adapter.function.SuperSelect
//import com.lhd.view.basewithme.R
//import com.lhd.view.basewithme.model.Account
//
//@SuperActionMenu
//@SuperDragVertical
//@SuperSelect(viewHandleSelectId = R.id.imgAccount, handleByLongClick = false)
//class AccountListAdapter :
//    SuperListAdapter<Account>(R.layout.item_account, object : DiffUtil.ItemCallback<Account>() {
//        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
//            return oldItem.name == newItem.name
//        }
//
//    }) {
//
//}