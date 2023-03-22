package com.example.database.standings.converter

import androidx.room.TypeConverter
import com.example.database.standings.model.StandingItemEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsDataConverter {

    @TypeConverter
    fun fromStandingsList(value: List<StandingItemEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StandingItemEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStandingsList(value: String): List<StandingItemEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<StandingItemEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}
