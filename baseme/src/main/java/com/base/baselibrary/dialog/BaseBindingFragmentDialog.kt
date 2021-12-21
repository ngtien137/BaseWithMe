package com.base.baselibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.base.baselibrary.R
import com.base.baselibrary.views.ext.loge
import kotlinx.coroutines.*

abstract class BaseBindingFragmentDialog<BD : ViewDataBinding> :
    BaseFragmentDialog() {

    protected var _binding: BD? = null
    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!

    override fun getCustomTheme(): Int = STYLE_NORMAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(context), layoutId, null, false)
        _binding!!.lifecycleOwner = this
        initBinding()
        val background = _binding!!.root.findViewById<View>(R.id.backgroundDialog)
        background?.setOnClickListener { dismiss() }
        return _binding?.root
    }

    abstract fun initBinding()

    override fun setUp(view: View?) {
        delayDismiss()
    }


    //region dismiss job
    private var dismissJob: Job? = null

    open fun timeDismissJob():Long{
        return -1L
    }

    private fun delayDismiss(){
        if(timeDismissJob()!=-1L) {
            dismissJob = lifecycleScope.launchWhenStarted {
                withContext(Dispatchers.IO) {
                    delay(timeDismissJob())
                }
                findNavController().navigateUp()
            }
        }
    }
    //end region

    override fun onDestroyView() {
        dismissJob?.cancel()
        super.onDestroyView()
        _binding = null
    }

}
