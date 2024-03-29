package com.example.database.standings.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.database.teams.model.TeamEntity
import com.google.gson.annotations.SerializedName

data class StandingItemEntity(
    @Embedded(prefix = "information_all_") val all: InformationStandingEntity,
    @Embedded(prefix = "information_away_") val away: InformationStandingEntity,
    @Embedded(prefix = "information_home_") val home: InformationStandingEntity,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "form") val form: String,
    @ColumnInfo(name = "goalsDiff") val goalsDiff: Int,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "points") val points: Int,
    @ColumnInfo(name = "rank") val rank: Int,
    @ColumnInfo(name = "status") val status: String,
    @Embedded(prefix = "team_") val team: TeamEntity,
    @SerializedName("update") val update: String
)
