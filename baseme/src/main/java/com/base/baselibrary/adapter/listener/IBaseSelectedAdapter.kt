package com.base.baselibrary.adapter.listener

interface IBaseSelectedAdapter<T> : ListItemListener {
    fun onItemSelected(item: T, position: Int)
}