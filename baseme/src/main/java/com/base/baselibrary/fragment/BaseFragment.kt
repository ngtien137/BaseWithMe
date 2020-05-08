package com.base.baselibrary.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.R
import java.lang.Exception


abstract class BaseFragment<BD : ViewDataBinding, A : AppCompatActivity> : Fragment() {
    /**
     * Normal base fragment
     */
    protected lateinit var binding: BD

    val activity by lazy {
        context as A
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                getLayoutId(), container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
        initView()
    }

    abstract fun getLayoutId(): Int

    open fun initView() {

    }

    open fun initBinding() {

    }

    fun popBackStack(tag: String) {
        val backTag = if (tag.isEmpty()) javaClass.simpleName else tag
        activity.supportFragmentManager.popBackStack(
                backTag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

}