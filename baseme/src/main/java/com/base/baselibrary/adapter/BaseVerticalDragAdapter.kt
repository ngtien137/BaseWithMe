package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR
import com.base.baselibrary.model.ModelBase

open class BaseVerticalDragAdapter<T: Any>(private @LayoutRes val resLayout: Int)

    : RecyclerView.Adapter<BaseVerticalDragAdapter.ViewHolderBase>() {

    private lateinit var inflater:LayoutInflater

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
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

    open fun onMoveItem(touchPosition:Int,targetPosition:Int){

    }

    open fun onMovedItem(touchPosition:Int,targetPosition:Int){

    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        val item = data?.get(position)
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
