package com.example.database.userpreferences.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by jrzeznicki on 13/02/2023.
 */
class UserPreferencesConverter {

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }
}
