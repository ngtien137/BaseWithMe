package com.base.baselibrary.adapter.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewHolderBase: RecyclerView.ViewHolder {
    val binding: ViewDataBinding

    constructor(binding: ViewDataBinding) : super(binding.root) {
        this.binding = binding
    }
}