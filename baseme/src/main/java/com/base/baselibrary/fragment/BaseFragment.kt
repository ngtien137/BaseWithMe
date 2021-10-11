package com.base.baselibrary.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
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
import com.base.baselibrary.utils.SDKUtils.isBuildLargerThan
import java.lang.Exception


abstract class BaseFragment<BD : ViewDataBinding, A : AppCompatActivity> : Fragment(),
    View.OnClickListener {
    /**
     * Normal base fragment
     */
    open fun isSetCustomColorStatusBar() = false

    lateinit var binding: BD

    val rootActivity by lazy {
        activity as A
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
        binding.setVariable(BR.viewListener, this as View.OnClickListener)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isConfiguredFullScreenState())
            checkFullScreenMode()
        if (isSetCustomColorStatusBar())
            setStatusBarColor(getStatusBarColor(), isDarkTheme())
        initView()
    }

    fun setStatusBarColor(color: Int = Color.BLACK, darkTheme: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity?.window?.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUIVisibility = decorView.systemUiVisibility
                newUIVisibility = if (darkTheme) {
                    newUIVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                } else {
                    newUIVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

                decorView.systemUiVisibility = newUIVisibility
                this.statusBarColor = color
            }
        } else if (isBuildLargerThan(Build.VERSION_CODES.R)) {
            activity?.window?.apply {
                if (darkTheme) {
                    decorView.windowInsetsController?.setSystemBarsAppearance(
                        0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    decorView.windowInsetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
                this.statusBarColor = color
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun checkFullScreenMode() {
        if (isFullScreen()) {
            activity?.window?.apply {
                if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                    setDecorFitsSystemWindows(false)
                    insetsController?.hide(WindowInsets.Type.statusBars())
                    insetsController?.show(WindowInsets.Type.navigationBars())
                    insetsController?.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
                }
            }
        } else
//        if (isClearFullScreen())
        {
            activity?.window?.apply {
                if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                    setDecorFitsSystemWindows(true)
                    insetsController?.show(WindowInsets.Type.statusBars())
                    insetsController?.show(WindowInsets.Type.navigationBars())
                    insetsController?.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
                    if (isDarkTheme()) {
                        decorView.windowInsetsController?.setSystemBarsAppearance(
                            0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        decorView.windowInsetsController?.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                    this.statusBarColor = this@BaseFragment.getStatusBarColor()
                } else {
                    clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    decorView.systemUiVisibility = if (isDarkTheme()) {
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()) and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                    } else {
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }

            }
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    @ColorInt
    open fun getStatusBarColor(): Int = Color.WHITE

    open fun isDarkTheme(): Boolean = true

    //open fun isClearFullScreen(): Boolean = false

    open fun isConfiguredFullScreenState() = true

    open fun isFullScreen() = false

    open fun initView() {

    }

    open fun initBinding() {

    }

    fun popBackStack(tag: String) {
        val backTag = if (tag.isEmpty()) javaClass.simpleName else tag
        rootActivity.supportFragmentManager.popBackStack(
            backTag,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun onClick(v: View?) {
        v?.let {
            onViewClick(it.id)
        }
    }

    open fun onViewClick(vId: Int) {

    }

    override fun onResume() {
        super.onResume()
    }

}