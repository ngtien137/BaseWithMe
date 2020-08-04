package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.BR
import com.base.baselibrary.adapter.function.SuperActionMenu
import com.base.baselibrary.adapter.function.SuperDragVertical
import com.base.baselibrary.adapter.function.SuperSelect
import com.base.baselibrary.adapter.listener.ISuperAdapterListener
import com.base.baselibrary.adapter.listener.IDragVerticalAdapter
import com.base.baselibrary.adapter.listener.ISelectedSuperAdapter
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.adapter.viewholder.SuperHolderBase
import com.base.baselibrary.utils.onDebouncedClick
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperCallback
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperExtension
import com.base.baselibrary.views.rv_touch_helper.VerticalDragTouchHelper
import java.util.*

open class SuperAdapter<T : Any>(@LayoutRes private val resLayout: Int) :
    RecyclerView.Adapter<SuperHolderBase>(), ISelectedSuperAdapter, IDragVerticalAdapter {

    private var annotationSelected: SuperSelect? = null
    private var annotationDragVertical: SuperDragVertical? = null
    private var annotationActionMenu: SuperActionMenu? = null

    var listSelected: Stack<T>
        private set

    var liveListSelected = MutableLiveData<Stack<T>>()

    private var lastSelectedPosition = -1

    private lateinit var inflater: LayoutInflater

    private var itemTouchHelperActionMenu: ItemTouchHelperExtension? = null

    var modeSelected = false
        private set
    var liveModeSelected = MutableLiveData(false)
        private set

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    fun setListSelected(liveListSelected: MutableLiveData<Stack<T>>) {
        this.liveListSelected = liveListSelected
        listSelected = liveListSelected.value!!
        notifyDataSetChanged()
    }

    init {
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is SuperSelect -> {
                    annotationSelected = annotation
                }
                is SuperDragVertical -> {
                    annotationDragVertical = annotation
                }
                is SuperActionMenu -> {
                    annotationActionMenu = annotation
                }
                else -> {
                }
            }
        }
        listSelected = Stack()
        liveListSelected.value = listSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHolderBase {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resLayout, parent, false
        )
        return SuperHolderBase(binding, annotationActionMenu)
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
            holder.binding.setVariable(BR.modeSelected, liveModeSelected)
            holder.binding.lifecycleOwner = holder.binding.root.context as LifecycleOwner
            holder.binding.executePendingBindings()
            checkSelected(holder)
            onConfigViewHolder(holder, holder.adapterPosition)
        }
    }

    override fun selectedAll() {
        changeModeSelect(true)
        listSelected.clear()
        listSelected.addAll(data ?: ArrayList())
        notifyItemRangeChanged(0, data?.size ?: 0)
    }

    override fun clearAllSelect() {
        listSelected.clear()
        notifyItemRangeChanged(0, data?.size ?: 0)
        lastSelectedPosition = -1
        changeModeSelect(false)
    }

    fun clearSelectWithoutPayloads() {
        listSelected.clear()
        lastSelectedPosition = -1
        notifyDataSetChanged()
        changeModeSelect(false)
    }

    open fun onHandleLongClickToCheck(item: T, holder: SuperHolderBase): Boolean {
        if (annotationSelected?.validCheckAgainAfterEnableSelectedByLongClick == true) {
            checkValidateCheckWithListener(item, holder)
        }
        return true
    }

    private fun checkValidateCheckWithListener(item: T, holder: SuperHolderBase) {
        if (listener != null && listener is ISuperAdapterListener<*>) {
            if ((listener as ISuperAdapterListener<T>).onValidateBeforeCheckingItem(
                    item,
                    holder.adapterPosition
                )
            ) {
                validateCheck(item, holder)
            }
        } else
            validateCheck(item, holder)
    }

    private fun getItem(position: Int) = data?.get(position)

    private fun checkSelected(holder: SuperHolderBase) {
        annotationSelected?.let { annotationSelected ->
            if (annotationSelected.viewHandleSelectId != -1) {
                getItem(holder.adapterPosition)?.let { item ->
                    holder.itemView.findViewById<View>(annotationSelected.viewHandleSelectId)?.let {
                        val selected = listSelected.search(item) != -1
                        onConfigSelected(holder, holder.adapterPosition, selected)
                        if (annotationSelected.handleByLongClick) {
                            it.setOnLongClickListener {
                                if (!modeSelected)
                                    changeModeSelect(true)
                                onHandleLongClickToCheck(item, holder)
                            }
                            it.onDebouncedClick {
                                if (modeSelected)
                                    checkValidateCheckWithListener(item, holder)
                                else {
                                    checkViewIdHandleSelectSingleClick(holder.adapterPosition)
                                }
                            }
                        } else {
                            it.onDebouncedClick {
                                if (!modeSelected)
                                    changeModeSelect(true)
                                if (modeSelected)
                                    checkValidateCheckWithListener(item, holder)
                                else {
                                    checkViewIdHandleSelectSingleClick(holder.adapterPosition)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkViewIdHandleSelectSingleClick(position: Int) {
        (listener as ISuperAdapterListener<T>?)?.onViewHandleCheckClicked(
            getItem(position) as T,
            position
        )
    }

    private fun changeModeSelect(select: Boolean) {
        modeSelected = select
        liveModeSelected.value = select
    }

    private fun validateCheck(item: T, holder: SuperHolderBase) {
        var selected = listSelected.search(item) != -1
        if (annotationSelected!!.enableSelectItemMultipleTime) {
            selected = false
        }
        if (selected) {
            if (annotationSelected!!.enableUnSelect) {
                listSelected.remove(item)
                lastSelectedPosition = -1
                notifyItemChanged(holder.adapterPosition, 1)
                selected = false
                if (listSelected.isEmpty() && annotationSelected!!.disableSelectModeWhenEmpty) {
                    changeModeSelect(false)
                }
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
        liveListSelected.value = listSelected
        if (listener is ISuperAdapterListener<*>?) {
            val pos = holder.adapterPosition
            (listener as ISuperAdapterListener<T>?)?.onItemSelected(
                getItem(pos) as T,
                pos,
                selected
            )
        }
    }

    override fun onMoveItem(touchPosition: Int, targetPosition: Int) {
        annotationDragVertical?.let { annotationDragVertical ->
            (listener as ISuperAdapterListener<T>?)?.onItemSwap(touchPosition, targetPosition)
            data?.let { mItems ->
                if (touchPosition < targetPosition) {
                    for (i in touchPosition until targetPosition) {
                        Collections.swap(mItems, i, i + 1)
                        notifyItemChanged(i, 0)
                        notifyItemChanged(i + 1, 0)
                    }
                } else {
                    for (i in touchPosition downTo targetPosition + 1) {
                        Collections.swap(mItems, i, i - 1)
                        notifyItemChanged(i, 0)
                        notifyItemChanged(i - 1, 0)
                    }
                }
                notifyItemMoved(touchPosition, targetPosition)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (annotationDragVertical != null) {
            val callback = VerticalDragTouchHelper(this)
            ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
        }
        if (annotationActionMenu != null) {
            val callback = ItemTouchHelperCallback()
            itemTouchHelperActionMenu = ItemTouchHelperExtension(callback).apply {
                attachToRecyclerView(recyclerView)
            }
        }
    }

    fun closeAllActionMenu() {
        itemTouchHelperActionMenu?.closeOpened()
    }

}