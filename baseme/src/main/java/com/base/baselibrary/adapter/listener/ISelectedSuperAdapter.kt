package com.base.baselibrary.adapter.listener

import androidx.annotation.IdRes
import com.base.baselibrary.adapter.viewholder.SuperHolderBase

interface ISelectedSuperAdapter {
    open fun onConfigPayLoad(
        holder: SuperHolderBase,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }

    open fun onConfigViewHolder(holder: SuperHolderBase, adapterPosition: Int) {}

    open fun onConfigSelected(
        holder: SuperHolderBase,
        adapterPosition: Int,
        selected: Boolean
    ) {
    }

    fun selectedAll()

    fun clearAllSelect()

}