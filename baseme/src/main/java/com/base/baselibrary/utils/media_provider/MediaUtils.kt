package com.base.baselibrary.utils.media_provider

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import java.lang.reflect.Field

inline fun <reified T : MediaModelBase> Context.getMedia(
    onCheckIfAddItem: (currentList: List<T>, item: T) -> Boolean = { _, _ -> true },
    onCheckContinueLoad: (currentList: List<T>, item: T) -> Boolean = { _, _ -> true },
    projection: Array<String>? = null,
    selection: String? = null,
    selectArgs: Array<String>? = null,
    sortOrder: String? = null
): MutableList<T> {
    val media = T::class.java.newInstance()
    val uri = media.getUri()
    val cursor = contentResolver.query(
        uri,
        projection,
        selection,
        selectArgs,
        sortOrder
    )
    val data = mutableListOf<T>()
    cursor?.moveToFirst()
    while (cursor?.isAfterLast == false) {
        val item = getRow<T>(cursor)
        if (onCheckIfAddItem(data, item)) {
            data.add(item)
        }
        if (!onCheckContinueLoad(data, item)) {
            break
        }
        cursor.moveToNext()
    }
    return data
}


inline fun <reified T : MediaModelBase> getRow(cursor: Cursor?): T {
    val t = T::class.java.newInstance()
    val fields = t.javaClass.declaredFields
    fields.forEach {
        it.isAccessible = true
        val annotation = it.getAnnotation(MediaInfo::class.java)
        if (annotation != null) {
            val index = cursor?.getColumnIndex(annotation.getFieldName)
            mappingField(cursor!!, index!!, it, t)
        }
    }
    return t
}

fun <T : MediaModelBase> mappingField(
    cursor: Cursor,
    index: Int, f: Field, t: T
) {
    when (f.type) {
        Int::class.java
        -> f.setInt(t, cursor.getInt(index))
        String::class.java
        -> f.set(t, cursor.getString(index))
        Float::class.java
        -> f.setFloat(t, cursor.getFloat(index))
        Long::class.java
        -> f.setLong(t, cursor.getLong(index))
    }
}

fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}