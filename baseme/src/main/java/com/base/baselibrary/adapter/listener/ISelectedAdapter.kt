package com.base.baselibrary.adapter.listener

import androidx.annotation.IdRes
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import java.util.*

interface ISelectedAdapter {
    open fun onConfigPayLoad(
        holder: ViewHolderBase,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }

    open fun onConfigViewHolder(holder: ViewHolderBase, adapterPosition: Int) {}

    open fun onConfigSelected(
        holder: ViewHolderBase,
        adapterPosition: Int,
        selected: Boolean
    ) {
    }

    @IdRes
    open fun getViewHandleSelected(): Int = -1

    open fun enableUnSelected(): Boolean = true

    open fun enableMultipleSelected(): Boolean = false

    fun selectedAll()

    fun clearAllSelect()

}