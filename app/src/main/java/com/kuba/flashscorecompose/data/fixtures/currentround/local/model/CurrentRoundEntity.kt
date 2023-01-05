package com.kuba.flashscorecompose.data.fixtures.currentround.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Entity(tableName = "current_rounds")
data class CurrentRoundEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "league_id") val leagueId: Int,
    @ColumnInfo(name = "season") val season: Int,
    @ColumnInfo(name = "round") val round: String
)