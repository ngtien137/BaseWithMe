package com.base.baselibrary.views.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.base.baselibrary.R

class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circlePaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
    }

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.CircleView)
            circlePaint.color = ta.getColor(R.styleable.CircleView_cv_circle_color,Color.TRANSPARENT)
            ta.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let{
            drawView(it)
        }
    }

    private fun drawView(canvas:Canvas){
        if (width>0&&height>0){
            var radius = width/2f
            if (height>width)
                radius = height/2f
            canvas.drawCircle(width/2f,height/2f,radius,circlePaint)
        }
    }

}