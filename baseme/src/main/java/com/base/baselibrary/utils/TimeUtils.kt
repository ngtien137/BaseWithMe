package com.base.baselibrary.utils

import com.base.baselibrary.views.ext.loge
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private const val timeStampPerDay = 86400000L
    private const val apiDateFormat = "yyyy-MM-dd"
    private const val normalDateFormat = "dd-MM-yyyy"
    private const val normal2DateFormat = "dd/MM/yyyy"

    private fun formatTime(tt: Int): String? {
        return String.format("%02d", tt)
    }

    fun convertsTime(
        diff: Long,
        minHourFormat: Boolean = false,
        isCanBeZero: Boolean = true
    ): String {
        val diffMilliseconds = (diff % 1000).toInt()
        var diffSeconds = (diff / 1000 % 60).toInt()
        val diffMinutes = (diff / (60 * 1000) % 60).toInt()
        val diffHours = (diff / (60 * 60 * 1000) % 24).toInt()
        val diffDays = (diff / (24 * 60 * 60 * 1000)).toInt()
        var str = ""
        if (!isCanBeZero && diffDays == 0 && diffHours == 0 && diffMinutes == 0 && diffSeconds == 0 && diff > 0) {
            diffSeconds = 1;
        }
        str = if (diffDays > 0) Integer.toString(diffDays) + "d" + " " +
                formatTime(diffHours) + ":" + formatTime(
            diffMinutes
        ) + ":" +
                formatTime(diffSeconds) else if (diffHours > 0 || minHourFormat) formatTime(
            diffHours
        ) + ":" + formatTime(
            diffMinutes
        ) + ":" +
                formatTime(diffSeconds) else formatTime(
            diffMinutes
        ) + ":" + formatTime(
            diffSeconds
        )
        return str
    }

    fun Date.toFormat(
        stringFormat: String = apiDateFormat,
        locale: Locale = Locale.getDefault()
    ): String {
        val format = SimpleDateFormat(stringFormat, locale)
        return format.format(this)
    }

    fun String.toDate(
        stringFormat: String = apiDateFormat,
        locale: Locale = Locale.getDefault()
    ): Date {
        val format = SimpleDateFormat(stringFormat, locale)
        var res = Date()
        try {
            res = format.parse(this)!!
        } catch (e: ParseException) {

        }
        return res
    }

    fun Long.toStringDateFormat(
        stringFormat: String = apiDateFormat,
        locale: Locale = Locale.getDefault(),
        notUpcaseAll: Boolean = true
    ): String {
        val timeStamp = this
        val format = SimpleDateFormat(stringFormat, locale)
        val date = Calendar.getInstance().apply {
            timeInMillis = timeStamp
        }.time ?: Date()
        return format.format(date)
    }

    fun countLengthBetween(fromDate: Date, toDate: Date): Int {
        return ((toDate.time - fromDate.time) / timeStampPerDay).toInt()
    }
}
