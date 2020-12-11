package com.lhd.view.basewithme.adapter.live

import androidx.lifecycle.MutableLiveData
import com.base.baselibrary.adapter.SuperAdapter
import com.base.baselibrary.adapter.viewholder.SuperHolderBase
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.ItemLiveObjectBinding
import com.lhd.view.basewithme.model.LiveObject

class AdapterLive : SuperAdapter<LiveObject>(R.layout.item_live_object) {

    val liveIndexGone = MutableLiveData("")

    override fun onConfigViewHolder(holder: SuperHolderBase, adapterPosition: Int) {
        (holder.binding as ItemLiveObjectBinding).let { binding ->
            binding.indexGone = liveIndexGone
        }
        super.onConfigViewHolder(holder, adapterPosition)
    }

}