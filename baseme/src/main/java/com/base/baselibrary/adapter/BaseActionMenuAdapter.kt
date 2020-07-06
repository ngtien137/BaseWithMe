package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR
import com.base.baselibrary.R
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.views.rv_touch_helper.ITouchHelperExtension

open class BaseActionMenuAdapter<T: Any>(@LayoutRes private val resLayout: Int)

    : RecyclerView.Adapter<BaseActionMenuAdapter.ActionViewHolderBase>() {

    private lateinit var inflater:LayoutInflater

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolderBase {
        if(!::inflater.isInitialized){
            inflater = LayoutInflater.from(parent.context)
        }
         val binding = DataBindingUtil.inflate<ViewDataBinding>(
             inflater, resLayout, parent, false
         )
        return ActionViewHolderBase(binding)
    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ActionViewHolderBase, position: Int) {
        val item = data?.get(position)
        holder.binding.setVariable(BR.item, item)
        holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
        holder.binding.setVariable(BR.listener, listener)
        holder.binding.executePendingBindings()
    }

    class ActionViewHolderBase: RecyclerView.ViewHolder,ITouchHelperExtension {
        val binding: ViewDataBinding

        constructor(binding: ViewDataBinding) : super(binding.root) {
            this.binding = binding
        }

        fun getLayoutMenu() = binding.root.findViewById<ViewGroup>(R.id.layout_item_menu_action)
        fun getLayoutMainContent() = binding.root.findViewById<ViewGroup>(R.id.layout_item_main_content)

        override fun getLayoutMenuWidth(): Float {
            return getLayoutMenu()?.width?.toFloat()?:0f
        }
    }
}
