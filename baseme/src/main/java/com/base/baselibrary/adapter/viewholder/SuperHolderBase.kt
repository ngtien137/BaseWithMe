package com.base.baselibrary.adapter.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SuperHolderBase : RecyclerView.ViewHolder {
    val binding: ViewDataBinding

    constructor(binding: ViewDataBinding) : super(binding.root) {
        this.binding = binding
    }
}