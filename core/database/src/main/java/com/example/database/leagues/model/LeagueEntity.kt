package com.example.database.leagues.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 21/12/2022.
 */
@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "logo") val logo: String,
    @ColumnInfo(name = "countryCode") val countryCode: String,
    @ColumnInfo(name = "countryName") val countryName: String,
    @ColumnInfo(name = "countryFlag") val countryFlag: String,
    @ColumnInfo(name = "season") val season: Int,
    @ColumnInfo(name = "round") val round: String
)