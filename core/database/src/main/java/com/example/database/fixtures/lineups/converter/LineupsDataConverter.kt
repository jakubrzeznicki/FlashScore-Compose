package com.example.database.fixtures.lineups.converter

import androidx.room.TypeConverter
import com.example.database.players.model.PlayerEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by jrzeznicki on 04/01/2023.
 */
class LineupsDataConverter {

    @TypeConverter
    fun fromPlayerList(value: List<PlayerEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PlayerEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPlayerList(value: String): List<PlayerEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<PlayerEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}
