package com.base.baselibrary.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils

object BaseText {

    fun convertHtmlStringToText(htmlString: String): String {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(htmlString)
        }
        val chars = CharArray(spanned.length)
        TextUtils.getChars(spanned, 0, spanned.length, chars, 0)
        return String(chars)
    }

    fun textToHtmlSpanned(text: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(text)
        }
    }

}