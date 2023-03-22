package com.example.database.fixtures.statistics.converter

import androidx.room.TypeConverter
import com.example.database.fixtures.statistics.model.StatisticRowEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by jrzeznicki on 04/01/2023.
 */
class StatisticsDataConverter {

    @TypeConverter
    fun fromStatisticsList(value: List<StatisticRowEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StatisticRowEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStatisticList(value: String): List<StatisticRowEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<StatisticRowEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}
