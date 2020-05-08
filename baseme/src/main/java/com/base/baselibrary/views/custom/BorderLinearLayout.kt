package com.base.baselibrary.views.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.base.baselibrary.R

class BorderLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    var borderCorner = 0f
    val rectBorder = RectF()

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.BorderLinearLayout)
            paintBorder.strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderLinearLayout_bll_border_size,0).toFloat()
            paintBorder.color = ta.getColor(R.styleable.BorderLayout_bl_border_color,
                Color.TRANSPARENT)
            borderCorner = ta.getDimensionPixelSize(R.styleable.BorderLinearLayout_bll_border_corner,0).toFloat()

            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val borderSizeHalf = paintBorder.strokeWidth/2
        rectBorder.set(0f+borderSizeHalf,0f+borderSizeHalf,w-borderSizeHalf,h.toFloat()-borderSizeHalf)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.drawRoundRect(rectBorder,borderCorner,borderCorner,paintBorder)
    }

}
