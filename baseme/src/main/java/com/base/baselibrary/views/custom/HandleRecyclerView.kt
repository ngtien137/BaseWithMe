package com.base.baselibrary.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class HandleRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    var isTouched = false

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when (e?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isTouched = true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTouched = false
            }
        }
        return super.onInterceptTouchEvent(e)
    }

}