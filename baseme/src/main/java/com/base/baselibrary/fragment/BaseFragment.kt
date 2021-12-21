package com.base.baselibrary.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.base.baselibrary.BR
import com.base.baselibrary.utils.SDKUtils.isBuildLargerThan
import com.base.baselibrary.utils.Screen


abstract class BaseFragment<BD : ViewDataBinding, A : AppCompatActivity> : Fragment(),
    View.OnClickListener {
    /**
     * Normal base fragment
     */

    private var _binding: BD? = null

    val binding get() = _binding!!

    val rootActivity by lazy {
        activity as A
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            getLayoutId(), container, false
        )
        _binding?.lifecycleOwner = viewLifecycleOwner
        _binding?.setVariable(BR.viewListener, this as View.OnClickListener)
        initBinding()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    @LayoutRes
    abstract fun getLayoutId(): Int

    @ColorInt
    open fun getStatusBarColor(): Int = Color.WHITE

    open fun getTypeScreen(): Screen.TypeScreen = Screen.TypeScreen.NONE

    open fun isSetCustomColorStatusBar() = false

    open fun isDarkTheme(): Boolean = false

    open fun isClearBindingWhenDestroyView(): Boolean = true

    //open fun isClearFullScreen(): Boolean = false

    open fun isConfiguredFullScreenState() = true

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

    @Suppress("DEPRECATION")
    fun applyScreenType() {
        when (getTypeScreen()) {
            Screen.TypeScreen.NO_LIMIT -> {
                activity?.window?.apply {
                    if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                        setDecorFitsSystemWindows(false)
                        insetsController?.hide(WindowInsets.Type.statusBars())
                        insetsController?.hide(WindowInsets.Type.navigationBars())
                        insetsController?.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    } else {
                        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                // Hide the nav bar and status bar
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_FULLSCREEN)
                    }
                }
            }
            Screen.TypeScreen.FULL_SCREEN -> {
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
            }
            Screen.TypeScreen.NORMAL_SCREEN -> {
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
            Screen.TypeScreen.TRANSLUCENT_STATUS_BAR -> {
                if (isBuildLargerThan(Build.VERSION_CODES.M)) {
                    activity?.window?.apply {
                        if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                            setDecorFitsSystemWindows(false)
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
                        } else {
                            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                            decorView.systemUiVisibility = if (isDarkTheme()) {
                                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                            } else {
                                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            }
                        }
                    }

                }
            }
            Screen.TypeScreen.TRANSPARENT_STATUS_BAR -> {
                activity?.window?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                            setDecorFitsSystemWindows(true)
                            insetsController?.show(WindowInsets.Type.statusBars())
                            insetsController?.hide(WindowInsets.Type.navigationBars())
                        }
                        clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        //WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = state
                        statusBarColor = Color.TRANSPARENT
                    }
                }
            }
            Screen.TypeScreen.NONE -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        applyScreenType()
        if (isSetCustomColorStatusBar())
            setStatusBarColor(getStatusBarColor(), isDarkTheme())
    }

    override fun onDestroyView() {
        if (getTypeScreen() == Screen.TypeScreen.TRANSPARENT_STATUS_BAR) {
            removeTransparentStatusBar()
        }
        super.onDestroyView()
        if (isClearBindingWhenDestroyView())
            _binding = null
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setTransparentStatusBar(
        color: Int = Color.BLACK,
        state: Boolean = true,
        isTransparent: Boolean = true
    ) {
        activity?.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                    setDecorFitsSystemWindows(true)
                    insetsController?.show(WindowInsets.Type.statusBars())
                    insetsController?.hide(WindowInsets.Type.navigationBars())
                }


                clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                    state
                statusBarColor = if (isTransparent) Color.TRANSPARENT else color
            }
        }
    }

    private fun removeTransparentStatusBar() {
        val window = rootActivity.window
        if (window != null) {
            val decorView = window.decorView
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

}