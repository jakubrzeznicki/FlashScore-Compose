package com.example.database.teams.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "logo") val logo: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "winner") val isWinner: Boolean,
    @ColumnInfo(name = "is_home") val isHome: Boolean,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "founded") val founded: Int,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "is_national") val isNational: Boolean,
    @ColumnInfo(name = "league_id") val leagueId: Int,
    @ColumnInfo(name = "season") val season: Int
)
