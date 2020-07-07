package com.base.baselibrary.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.base.baselibrary.BR
import com.base.baselibrary.adapter.listener.IBaseSelectedAdapter
import com.base.baselibrary.adapter.listener.ISelectedAdapter
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import java.util.*

open class BaseSelectedListAdapter<T : Any>(
    @LayoutRes private val resLayout: Int,
    diffItemCallBack: DiffUtil.ItemCallback<T>
) : BaseListAdapter<T>(resLayout, diffItemCallBack),
    ISelectedAdapter {

    var listSelected: Stack<T> = Stack()
        private set
    private var lastSelectedPosition = -1

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.setVariable(BR.listSelected, listSelected)
        holder.binding.executePendingBindings()
        checkSelected(holder, position)
        onConfigViewHolder(holder, holder.adapterPosition)
    }

    override fun onBindViewHolder(
        holder: ViewHolderBase,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            checkSelected(holder, position)
            onConfigPayLoad(holder, position, payloads)
        }
    }

    private fun checkSelected(holder: ViewHolderBase, position: Int) {
        if (getViewHandleSelected() != -1) {
            getItem(holder.adapterPosition)?.let { item ->
                holder.itemView.findViewById<View>(getViewHandleSelected())?.let {
                    var selected = listSelected.search(item) != -1
                    onConfigSelected(holder, holder.adapterPosition, selected)
                    it.setOnClickListener {
                        if (selected) {
                            if (enableUnSelected()) {
                                listSelected.remove(item)
                                lastSelectedPosition = -1
                                notifyItemChanged(holder.adapterPosition, 1)
                                selected = false
                            }
                        } else {
                            val oldPosition = lastSelectedPosition
                            listSelected.push(item)
                            lastSelectedPosition = holder.adapterPosition
                            if (!enableMultipleSelected() && oldPosition != -1) {
                                listSelected.remove(getItem(oldPosition))
                                notifyItemChanged(oldPosition, 1)
                            }
                            selected = true
                            notifyItemChanged(lastSelectedPosition, 1)
                        }
                        if (listener is IBaseSelectedAdapter<*>?) {
                            val pos = holder.adapterPosition
                            (listener as IBaseSelectedAdapter<T>?)?.onItemSelected(
                                getItem(pos) as T,
                                pos,
                                selected
                            )
                        }
                    }
                }
            }
        }
    }

    override fun selectedAll() {
        listSelected.clear()
        listSelected.addAll(currentList)
        notifyItemRangeChanged(0, currentList.size ?: 0)
    }

    override fun clearAllSelect() {
        listSelected.clear()
        notifyItemRangeChanged(0, currentList.size ?: 0)
    }

}