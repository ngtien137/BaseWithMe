package com.base.baselibrary.utils.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.renderscript.*
import android.view.View
import java.io.InputStream
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

object BaseBitmapUtils {

    fun rotateImageIfRequired(imagePath: String): Bitmap? {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val ei: ExifInterface = ExifInterface(imagePath)
        return rotateImageIfRequired(ei, bitmap)
    }

    fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
        val input: InputStream =
            context.contentResolver.openInputStream(selectedImage) ?: return null
        val ei: ExifInterface =
            if (Build.VERSION.SDK_INT > 23) ExifInterface(input) else ExifInterface(
                selectedImage.path ?: ""
            )
        return rotateImageIfRequired(ei, img)
    }

    private fun rotateImageIfRequired(ei: ExifInterface, img: Bitmap): Bitmap? {
        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> null
        }
    }

    private fun rotateImage(img: Bitmap, degree: Number): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

}