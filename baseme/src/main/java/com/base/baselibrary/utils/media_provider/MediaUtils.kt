package com.base.baselibrary.utils.media_provider

import android.content.Context
import android.database.Cursor
import java.lang.reflect.Field

fun <T : MediaModelBase> Context.getMedia(
    clz: Class<T>,
    onCheckIfAddItem:(item:T)->Boolean={true},
    projection: Array<String>? = null,
    selection: String? = null,
    selectArgs: Array<String>? = null,
    sortOrder: String? = null
): MutableList<T> {
    val media = clz.newInstance()
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
        val item = getRow(cursor, clz)
        if (onCheckIfAddItem(item)){
            data.add(item)
        }
        cursor.moveToNext()
    }
    return data
}



private fun <T : MediaModelBase> getRow(cursor: Cursor?, clz: Class<T>): T {
    val t = clz.newInstance()
    val fields = t.javaClass.declaredFields
    fields.forEach {
        it.isAccessible = true
        val annotation = it.getAnnotation(MediaInfo::class.java)
        if (annotation!=null) {
            val index = cursor?.getColumnIndex(annotation.getFieldName)
            mappingField(cursor!!, index!!, it, t)
        }
    }
    return t
}

private fun <T : MediaModelBase> mappingField(
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