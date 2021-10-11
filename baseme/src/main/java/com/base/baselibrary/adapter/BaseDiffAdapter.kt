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
import com.base.baselibrary.adapter.listener.ListItemListener

abstract class BaseDiffAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val inflater: LayoutInflater
) :
    ListAdapter<T, BaseDiffAdapter.ViewHolderBase<T>>(diffCallback) {
    // region Const and Fields
    var listener: ListItemDiffListener? = null
    // endregion

    // region override function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBase<T> {
        return ViewHolderBase(
            DataBindingUtil.inflate(
                inflater, viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderBase<T>, position: Int) =
        holder.bind(getItem(position), listener)
    // endregion

    // region ViewHolder
    class ViewHolderBase<T>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: T, diffListener: ListItemDiffListener?) {
            binding.apply {
                setVariable(BR.item, data)
                setVariable(BR.listener, diffListener)
                executePendingBindings()
            }
        }
    }
    // endregion

    // region listener
    interface ListItemDiffListener : ListItemListener
    // endregion
}

