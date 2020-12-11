package com.lhd.view.basewithme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.baselibrary.adapter.SuperAdapter
import com.base.baselibrary.fragment.BaseNavigationFragment
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.live.AdapterLive
import com.lhd.view.basewithme.databinding.FragmentListObjectLiveBinding
import com.lhd.view.basewithme.model.LiveObject
import com.lhd.view.basewithme.viewmodel.LiveObjectViewModel

class ListObjectLiveFragment : BaseNavFragment<FragmentListObjectLiveBinding>() {

    //region properties

    private val viewModel by autoViewModels<LiveObjectViewModel>()

    private val adapter by lazy {
        AdapterLive()
    }

    //endregion

    //region lifecycle

    override fun initBinding() {
        binding.adapter = adapter
    }

    override fun initView() {
        observer(viewModel.liveListObject) {
            adapter.data = it
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_object_live
    }

    //endregion

}