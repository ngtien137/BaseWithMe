package com.base.baselibrary.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.BR
import com.base.baselibrary.R
import java.lang.Exception


abstract class BaseFragment<BD : ViewDataBinding, A : AppCompatActivity> : Fragment(),View.OnClickListener {
    /**
     * Normal base fragment
     */
    open fun isSetCustomColorStatusBar() = false

    lateinit var binding: BD

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
        binding.setVariable(BR.viewListener,this as View.OnClickListener)
        initBinding()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isSetCustomColorStatusBar())
            setStatusBarColor(getStatusBarColor(), isDarkText())
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setStatusBarColor(color: Int = Color.BLACK, state: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUiVisibility = window.decorView.systemUiVisibility
                newUiVisibility = if (state) {
                    //Dark Text to show up on your light status bar
                    newUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    //Light Text to show up on your dark status bar
                    newUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                }
                window.decorView.systemUiVisibility = newUiVisibility
                window.statusBarColor = color
            }
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    @ColorInt
    open fun getStatusBarColor(): Int = Color.WHITE

    open fun isDarkText(): Boolean = true

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

    override fun onClick(v: View?) {
        v?.let {
            onViewClick(it.id)
        }
    }

    open fun onViewClick(vId:Int){

    }

}