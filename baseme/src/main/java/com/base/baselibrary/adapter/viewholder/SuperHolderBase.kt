package com.base.baselibrary.adapter.viewholder

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.R
import com.base.baselibrary.adapter.function.SuperActionMenu
import com.base.baselibrary.views.rv_touch_helper.ITouchHelperExtension

open class SuperHolderBase(
    val binding: ViewDataBinding,
    private val annotationActionMenu: SuperActionMenu?
) :
    RecyclerView.ViewHolder(binding.root), ITouchHelperExtension {

    fun getLayoutMenu(): ViewGroup? {
        val id = if (annotationActionMenu == null) -1 else {
            if (annotationActionMenu.menuId == -1)
                R.id.layout_item_menu_action
            else
                annotationActionMenu.menuId
        }
        return binding.root.findViewById(id)
    }

    fun getLayoutMainContent(): ViewGroup? {
        val id = if (annotationActionMenu == null) -1 else {
            if (annotationActionMenu.menuMainContent == -1)
                R.id.layout_item_main_content
            else
                annotationActionMenu.menuMainContent
        }
        return binding.root.findViewById(id)
    }

    override fun getLayoutMenuWidth(): Float {
        return getLayoutMenu()?.width?.toFloat() ?: 0f
    }
}