package com.base.baselibrary.views.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.base.baselibrary.R
import com.base.baselibrary.views.ext.set


class SimpleTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object{
        const val TV_DEF_COLOR = Color.BLACK
        var TV_DEF_TEXT_SIZE = 0
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = TV_DEF_COLOR
        typeface = ResourcesCompat.getFont(context, R.font.poppins)

    }
    private var gravity = Gravity.CENTER
    private val viewRect = RectF()
    private val textRect = Rect()
    private var text = ""

    init {
        TV_DEF_TEXT_SIZE = spToPx(12f)
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleTextView)
            textPaint.color = ta.getColor(R.styleable.SimpleTextView_tv_text_color, TV_DEF_COLOR)
            textPaint.textSize = ta.getDimensionPixelSize(R.styleable.SimpleTextView_tv_text_size,
                TV_DEF_TEXT_SIZE).toFloat()
            text = ta.getString(R.styleable.SimpleTextView_tv_text)?:""
            gravity = ta.getInt(R.styleable.SimpleTextView_tv_gravity,Gravity.CENTER)
            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.getTextBounds(text,0,text.length,textRect)
        viewRect.set(0f,0f,viewWidth,viewHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawView(canvas)
        }
    }

    private fun drawView(canvas:Canvas){
        val textWidth = textPaint.measureText(text)
        val textX = when(gravity){
            Gravity.LEFT->{
                textWidth/2f
            }
            Gravity.RIGHT->{
                viewRect.width()-textWidth/2f
            }
            else->{
                viewRect.centerX()
            }
        }
        //canvas.drawText(text,textX,viewRect.centerY()+textRect.height()/2f,textPaint)
        canvas.drawText(text,textX,viewRect.centerY()+textRect.height()/2f-(textRect.bottom-viewRect.top),textPaint)
    }

    private fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }

    class Gravity{
        companion object{
            const val CENTER = 0
            const val LEFT = 1
            const val RIGHT = 2
        }
    }
}