package com.base.baselibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.R

abstract class BaseBindingFragmentDialog<BD : ViewDataBinding>() :
    BaseFragmentDialog() {
    lateinit var binding: BD

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(context), layoutId, null, false)
        initBinding()
        val background = binding.root.findViewById<View>(R.id.backgroundDialog)
        background?.setOnClickListener { dismiss() }
        return binding.root
    }

    abstract fun initBinding()

}