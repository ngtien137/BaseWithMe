package com.lhd.view.basewithme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.databinding.FragmentTabBinding

class TabFragment : BaseNavFragment<FragmentTabBinding>() {

    var tab = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_tab
    }

    override fun initBinding() {
        binding.tabName = tab
    }

    override fun onViewClick(vId: Int) {
        when (vId) {
            R.id.btnOpenNewTab -> {
                navigateTo(R.id.action_pagerTestFragment_to_tabInfoFragment)
            }
        }
    }

}