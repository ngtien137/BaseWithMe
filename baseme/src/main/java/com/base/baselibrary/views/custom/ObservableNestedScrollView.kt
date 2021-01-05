package com.base.baselibrary.views.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

class ObservableNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    var onScrollToBottom: () -> Unit = {}

    init {
        setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val diff: Int = getChildAt(childCount - 1).bottom - (height + getScrollY())
            if (diff == 0) {
                onScrollToBottom()
            }
        }
    }
}