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
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.base.baselibrary.share_preference.BasePreference
import java.io.File


private var appInstance: Application? = null
private var basePreference: BasePreference? = null

fun Application.initBaseApplication() {
    appInstance = this
}

fun getApplication() = appInstance!!

fun Application.initPrefData(preferenceName: String) {
    basePreference = BasePreference(preferenceName, this)
}

//Example Long::class.java.getPrefData() or LongClass.getPrefData()
fun <T> Class<T>.getPrefData(key: String): T = basePreference!!.get(key, this)
fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
    basePreference!!.get(key, defaultValue, this)

fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)

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
    context?.getColor(colorRes) ?: Color.TRANSPARENT

fun getAppTypeFace(@FontRes fontId: Int, context: Context? = appInstance): Typeface? {
    if (context == null)
        return null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.resources.getFont(fontId)
    } else
        ResourcesCompat.getFont(context, fontId)
}

fun isConnectedInternet(context: Context? = appInstance): Boolean? {
    if (context == null)
        return null
    return InternetConnection.checkConnection(context)
}

fun Activity.openAppSetting(REQ: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, REQ)
}

fun Fragment.shareFiles(provider: String, vararg filePaths: String) {
    activity?.shareFiles(provider, *filePaths)
}

fun Fragment.shareFiles(provider: String, filePaths: List<String>) {
    activity?.shareFiles(provider, filePaths)
}

fun Context.shareFiles(provider: String, vararg filePaths: String) {
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
    doShareFile(uriArrayList)
}

fun Context.shareFiles(provider: String, filePaths: List<String>) {
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
    doShareFile(uriArrayList)
}

private fun Context.doShareFile(uriArrayList: ArrayList<Uri>) {
    if (uriArrayList.size > 0) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.type = "audio/*"
        startActivity(Intent.createChooser(intent, "Share files"))
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