package com.lhd.view.basewithme.adapter.media

import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.ItemMediaBinding
import com.lhd.view.basewithme.model.AppPhoto

class MediaAdapter : BaseAdapter<AppPhoto>(R.layout.item_media) {

    private lateinit var recyclerView: RecyclerView
    var mediaRowCount = 2
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder.binding as ItemMediaBinding).apply {
            clRoot.layoutParams.height = recyclerView.height / mediaRowCount
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}