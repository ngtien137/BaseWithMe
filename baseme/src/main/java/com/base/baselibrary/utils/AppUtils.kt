package com.base.baselibrary.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.base.baselibrary.share_preference.BasePreference

private var appInstance: Application? = null
private var basePreference: BasePreference? = null

fun Application.initBaseApplication() {
    appInstance = this
}

fun Application.initPrefData(preferenceName: String) {
    basePreference = BasePreference(preferenceName,this)
}

//Example Long::class.java.getPrefData() or LongClass.getPrefData()
fun <T> Class<T>.getPrefData(key:String):T = basePreference!!.get(key, this)
fun <T> Class<T>.getPrefData(key:String,defaultValue:T):T = basePreference!!.get(key, defaultValue,this)
fun <T> putPrefData(key:String,value:T) = basePreference!!.put(key,value)

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