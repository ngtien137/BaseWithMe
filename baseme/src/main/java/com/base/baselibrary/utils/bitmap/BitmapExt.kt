package com.base.baselibrary.utils.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.renderscript.*
import kotlin.math.roundToInt

fun Bitmap?.blurBitmap(radius: Float, context: Context?): Bitmap? {
    //Create renderscript
    val rs: RenderScript = RenderScript.create(context)

    //Create allocation from Bitmap
    val allocationIn: Allocation = Allocation.createFromBitmap(rs, this)
    val t: Type = allocationIn.type

    //Create allocation with the same type
    val allocationOut: Allocation = Allocation.createTyped(rs, t)

    //Create script
    val blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    //Set blur radius (maximum 25.0)
    blurScript.setRadius(radius)
    //Set input for script
    blurScript.setInput(allocationIn)
    //Call script for output allocation
    blurScript.forEach(allocationOut)

    //Copy script result into bitmap
    allocationOut.copyTo(this)

    //Destroy everything to free memory
    allocationIn.destroy()
    allocationOut.destroy()
    blurScript.destroy()
    //t.destroy()
    rs.destroy()
    return this
}

fun View.drawViewToBitmap(scale: Float = 1f): Bitmap? {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    if (scale != 1f) {
        return Bitmap.createScaledBitmap(
            bitmap, (bitmap.width * scale).roundToInt(),
            (bitmap.height * scale).roundToInt(), false
        )
    }
    return bitmap
}