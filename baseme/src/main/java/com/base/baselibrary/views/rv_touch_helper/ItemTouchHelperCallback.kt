package com.base.baselibrary.views.rv_touch_helper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.adapter.viewholder.SuperHolderBase

class ItemTouchHelperCallback : ItemTouchHelperExtension.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?
    ): Int {
        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START
        )
    }

    override fun onMove(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        target: RecyclerView.ViewHolder?
    ): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    override fun onChildDraw(
        c: Canvas?,
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (dY != 0f && dX == 0f) super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        if (viewHolder is SuperHolderBase) {
            val layoutMainContent = viewHolder.getLayoutMainContent() ?: return
            val tempDx = if (dX < -viewHolder.getLayoutMenuWidth()) {
                -viewHolder.getLayoutMenuWidth()
            } else dX
            layoutMainContent.translationX = tempDx
            //layoutMenu.translationX = tempDx
            return
        }
    }
}