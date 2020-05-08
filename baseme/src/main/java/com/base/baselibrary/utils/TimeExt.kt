package com.base.baselibrary.utils

import com.base.baselibrary.views.ext.loge
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val timeStampPerDay = 86400000L
val apiDateFormat = "yyyy-MM-dd"
val normalDateFormat = "dd-MM-yyyy"
val normal2DateFormat = "dd/MM/yyyy"

fun Date.toFormat(stringFormat: String= apiDateFormat, locale: Locale = Locale.getDefault()): String {
    val format = SimpleDateFormat(stringFormat, locale)
    return format.format(this)
}

fun String.toDate(stringFormat: String= apiDateFormat, locale: Locale = Locale.getDefault()): Date {
    val format = SimpleDateFormat(stringFormat, locale)
    var res = Date()
    try{
        res = format.parse(this)!!
    }catch (e: ParseException){

    }
    return res
}

fun Long.toStringDateFormat(stringFormat: String= apiDateFormat, locale: Locale = Locale.getDefault(), notUpcaseAll:Boolean=true): String {
    val timeStamp = this
    val format = SimpleDateFormat(stringFormat, locale)
    val date = Calendar.getInstance().apply {
        timeInMillis = timeStamp
    }.time?:Date()
    return format.format(date)
}

fun countLengthBetween(fromDate:Date,toDate:Date): Int {
    return ((toDate.time - fromDate.time)/ timeStampPerDay).toInt()
}