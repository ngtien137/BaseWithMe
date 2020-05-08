package com.base.baselibrary.utils

import androidx.room.TypeConverter
import com.google.gson.Gson


class StringListConverters{
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return arrayListOf(*objects)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}