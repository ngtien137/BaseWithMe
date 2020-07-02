package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR

open class BaseListAdapter<T: Any>(@LayoutRes private val resLayout: Int, diffItemCallBack:DiffUtil.ItemCallback<T>)

    : ListAdapter<T,BaseListAdapter.ViewHolderBase>(diffItemCallBack) {

    private lateinit var inflater:LayoutInflater

    var listener: ListItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBase {
        if(!::inflater.isInitialized){
            inflater = LayoutInflater.from(parent.context)
        }
         val binding = DataBindingUtil.inflate<ViewDataBinding>(
             inflater, resLayout, parent, false
         )
        return ViewHolderBase(binding)
    }


    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.binding.setVariable(BR.item, item)
        holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
        holder.binding.setVariable(BR.listener, listener)
        holder.binding.executePendingBindings()
    }

    class ViewHolderBase: RecyclerView.ViewHolder {
        val binding: ViewDataBinding

        constructor(binding: ViewDataBinding) : super(binding.root) {
            this.binding = binding
        }
    }

}
