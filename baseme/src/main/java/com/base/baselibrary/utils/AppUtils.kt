package com.base.baselibrary.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import java.io.File


var appInstance: BaseApplication? = null

fun BaseApplication.initBaseApplication() {
    appInstance = this
}

fun getApplication() = appInstance!!

suspend inline fun <reified T> putPrefData(key: String, value: T) =
    appInstance!!.baseDataStore.putPrefValue(key, value)

inline fun <reified T> getPrefData(key: String, value: T) =
    appInstance!!.baseDataStore.getPrefValue(key, value)

fun getAppString(@StringRes stringId: Int, context: Context? = appInstance): String {
    return context?.getString(stringId) ?: ""
}

fun getAppDrawable(@DrawableRes drawableId: Int, context: Context? = appInstance): Drawable? {
    if (context == null)
        return null
    return ContextCompat.getDrawable(context, drawableId)
}

fun getAppDimensionPixel(@DimenRes dimenId: Int, context: Context? = appInstance) =
    context?.resources?.getDimensionPixelSize(dimenId) ?: -1

fun getAppDimension(@DimenRes dimenId: Int, context: Context? = appInstance) =
    context?.resources?.getDimension(dimenId) ?: -1f

fun getAppColor(@ColorRes colorRes: Int, context: Context? = appInstance) =
    context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

fun getAppTypeFace(@FontRes fontId: Int, context: Context? = appInstance): Typeface? {
    if (context == null)
        return null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.resources.getFont(fontId)
    } else
        ResourcesCompat.getFont(context, fontId)
}

fun getAppAnimation(@AnimRes animId: Int, context: Context? = appInstance): Animation {
    return AnimationUtils.loadAnimation(context, animId)
}

fun getAppColors(@ArrayRes id: Int, context: Context? = appInstance) =
    context?.resources?.getStringArray(id) ?: arrayOf()

fun getAppDrawableIdByResourceName(
    name: String,
    packageName: String,
    context: Context? = appInstance
) = context?.resources?.getIdentifier(name, "drawable", packageName)

fun isConnectedInternet(context: Context? = appInstance): Boolean? {
    if (context == null)
        return null
    return InternetConnection.checkConnection(context)
}

fun Fragment.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, vararg filePaths: String
) {
    activity?.shareFiles(provider, typeShare, isShareWithNewTask, *filePaths)
}

fun Fragment.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, filePaths: List<String>
) {
    activity?.shareFiles(provider, typeShare, isShareWithNewTask, filePaths)
}

fun Context.shareFiles(
    provider: String, typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false, vararg filePaths: String
) {
    val uriArrayList: ArrayList<Uri> = ArrayList()
    filePaths.forEach {
        uriArrayList.add(
            FileProvider.getUriForFile(
                this,
                provider,
                File(it)
            )
        )
    }
    doShareFile(uriArrayList, typeShare, isShareWithNewTask)
}

fun Context.shareFiles(
    provider: String,
    typeShare: String = "audio/*",
    isShareWithNewTask: Boolean = false,
    filePaths: List<String>
) {
    val uriArrayList: ArrayList<Uri> = ArrayList()
    filePaths.forEach {
        uriArrayList.add(
            FileProvider.getUriForFile(
                this,
                provider,
                File(it)
            )
        )
    }
    doShareFile(uriArrayList, typeShare, isShareWithNewTask)
}

private fun Context.doShareFile(
    uriArrayList: ArrayList<Uri>,
    typeShare: String,
    isShareWithNewTask: Boolean
) {
    if (uriArrayList.size > 0) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.type = typeShare
        val intentChooser = Intent.createChooser(intent, "Share files")
        if (isShareWithNewTask) {
            intentChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intentChooser)
    }
}

fun checkWriteSystemSetting(context: Context = getApplication()): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
    else Settings.System.canWrite(context)
}

fun Activity.openWriteSettingPermission(requestCode: Int = 1000) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivityForResult(intent, requestCode)
    }
}

fun vibrateDevice() {
    getApplication().vibrateDevice()
}

fun Context.vibrateDevice() {
    val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        //deprecated in API 26
        v?.vibrate(500)
    }
}

val <T> T.exhaustive: T
    get() = this
