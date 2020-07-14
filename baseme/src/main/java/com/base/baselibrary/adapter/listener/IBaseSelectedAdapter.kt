package com.base.baselibrary.adapter.listener

interface IBaseSelectedAdapter<in T> : ListItemListener {
    fun onItemSelected(item: T, position: Int,selected:Boolean){}
    fun onItemClicked(item: T, position: Int)
}