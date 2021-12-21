package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.adapter.viewholder.ViewHolderBase

open class BaseHolderAdapter<T : Any, VH : ViewHolderBase>(@LayoutRes private val resLayout: Int)

    : RecyclerView.Adapter<VH>() {

    private lateinit var inflater: LayoutInflater

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resLayout, parent, false
        )
        return ViewHolderBase(binding) as VH
    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun getItem(itemPosition: Int) = data?.get(itemPosition)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data?.get(position)
        holder.binding.setVariable(BR.item, item)
        holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
        holder.binding.setVariable(BR.listener, listener)
        onConfigViewHolder(holder, holder.adapterPosition)
        val context = holder.binding.root.context
        if (getDefineLifecycleOwner() != null) {
            holder.binding.lifecycleOwner = getDefineLifecycleOwner()
        } else if (context is LifecycleOwner) {
            holder.binding.lifecycleOwner = context
        }
        holder.binding.executePendingBindings()
    }

    open fun onConfigViewHolder(holder: ViewHolderBase, adapterPosition: Int) {}

    open fun getDefineLifecycleOwner(): LifecycleOwner? {
        return null
    }

}
