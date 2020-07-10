package com.base.baselibrary.adapter.listener

interface IDragVerticalAdapter {
    fun onMoveItem(touchPosition: Int, targetPosition: Int) {}
    fun onMovedItem(touchPosition: Int, targetPosition: Int) {}
}