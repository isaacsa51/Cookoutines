package com.serranoie.android.core.data.utils

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun toString(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromString(string: String): List<String> {
        return string.split(",").filter { it.isNotEmpty() }
    }
}