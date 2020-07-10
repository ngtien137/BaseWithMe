package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR
import com.base.baselibrary.adapter.function.SuperSelect
import com.base.baselibrary.adapter.listener.IBaseSelectedAdapter
import com.base.baselibrary.adapter.listener.ISelectedSuperAdapter
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.adapter.viewholder.SuperHolderBase
import java.util.*

open class SuperAdapter<T : Any>(@LayoutRes private val resLayout: Int) :
    RecyclerView.Adapter<SuperHolderBase>(), ISelectedSuperAdapter {

    private var annotationSelected: SuperSelect? = null

    var listSelected: Stack<T> = Stack()
        private set
    private var lastSelectedPosition = -1

    private lateinit var inflater: LayoutInflater

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    init {
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is SuperSelect -> {
                    annotationSelected = annotation
                }
                else -> {
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHolderBase {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resLayout, parent, false
        )
        return SuperHolderBase(binding)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: SuperHolderBase, position: Int) {
        initBindingHolder(holder)
    }

    override fun onBindViewHolder(
        holder: SuperHolderBase,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            initBindingHolder(holder)
            onConfigPayLoad(holder, position, payloads)
        }
    }

    private fun initBindingHolder(holder: SuperHolderBase) {
        getItem(holder.adapterPosition).let { item ->
            holder.binding.setVariable(BR.item, item)
            holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
            holder.binding.setVariable(BR.listener, listener)
            holder.binding.setVariable(BR.listSelected, listSelected)
            holder.binding.executePendingBindings()
            checkSelected(holder)
        }
    }

    override fun selectedAll() {
        listSelected.clear()
        listSelected.addAll(data ?: ArrayList())
        notifyItemRangeChanged(0, data?.size ?: 0)
    }

    override fun clearAllSelect() {
        listSelected.clear()
        notifyItemRangeChanged(0, data?.size ?: 0)
        lastSelectedPosition = -1
    }

    open fun onHandleLongClickToCheck(item: T, holder: SuperHolderBase): Boolean {
        validateCheck(item, holder)
        return true
    }

    private fun getItem(position: Int) = data?.get(position)

    private fun checkSelected(holder: SuperHolderBase) {
        annotationSelected?.let { annotationSelected ->
            if (annotationSelected.viewHandleSelect != -1) {
                getItem(holder.adapterPosition)?.let { item ->
                    holder.itemView.findViewById<View>(annotationSelected.viewHandleSelect)?.let {
                        val selected = listSelected.search(item) != -1
                        onConfigSelected(holder, holder.adapterPosition, selected)
                        if (annotationSelected.handleByLongClick) {
                            it.setOnLongClickListener {
                                onHandleLongClickToCheck(item, holder)
                            }
                        } else {
                            it.setOnClickListener {
                                validateCheck(item, holder)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateCheck(item: T, holder: SuperHolderBase) {
        var selected = listSelected.search(item) != -1
        if (selected) {
            if (annotationSelected!!.enableUnSelect) {
                listSelected.remove(item)
                lastSelectedPosition = -1
                notifyItemChanged(holder.adapterPosition, 1)
                selected = false
            }
        } else {
            val oldPosition = lastSelectedPosition
            listSelected.push(item)
            lastSelectedPosition = holder.adapterPosition
            if (!annotationSelected!!.enableMultiSelect && oldPosition != -1) {
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

    open fun onMoveItem(touchPosition: Int, targetPosition: Int) {

    }

    open fun onMovedItem(touchPosition: Int, targetPosition: Int) {

    }
}