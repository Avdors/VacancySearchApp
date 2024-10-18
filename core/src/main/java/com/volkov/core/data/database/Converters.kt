package com.example.core.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }
}