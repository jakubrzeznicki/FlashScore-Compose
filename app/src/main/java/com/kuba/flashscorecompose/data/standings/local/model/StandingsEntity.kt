package com.kuba.flashscorecompose.data.standings.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity

@Entity(tableName = "standings", primaryKeys = ["primary_league_id", "season"])
data class StandingsEntity(
    @Embedded(prefix = "league_") val league: LeagueEntity,
    @ColumnInfo(name = "primary_league_id") val leagueId: Int,
    @ColumnInfo(name = "season") val season: Int,
    @ColumnInfo(name = "standings") val standings: List<StandingItemEntity>
)