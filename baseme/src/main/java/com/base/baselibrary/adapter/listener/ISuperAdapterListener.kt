package com.base.baselibrary.adapter.listener

interface ISuperAdapterListener<in T> : ListItemListener {
    fun onItemSelected(item: T, position: Int, selected: Boolean) {}
    fun onViewHandleCheckClicked(item: T, position: Int) {}
    fun onValidateBeforeCheckingItem(item: T, position: Int): Boolean {
        return true
    }

    fun onItemSwap(touchPosition: Int, targetPosition: Int) {}
}